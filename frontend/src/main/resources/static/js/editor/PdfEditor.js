import {EditorState} from "./EditorState.js";
import {HistoryManager} from "./HistoryManager.js";
import {CellOperations} from "./CellOperations.js";
import {EditorModes} from "./EditorModes.js";

import {SvgRenderer} from "../rendering/SvgRenderer.js";
import {PreviewRenderer} from "../rendering/PreviewRenderer.js";

/**
 * Main PDF editor controller.
 *
 * Coordinates:
 * - editor state
 * - rendering
 * - editing operations
 * - history
 * - mapping
 * - toolbox
 */
export class PdfEditor {

    /**
     * @param {Toolbox} toolbox
     * @param {ProcessingSession} workflowSession
     */
    constructor(toolbox, workflowSession) {
        this.state = new EditorState();

        this.history = new HistoryManager();
        this.workflowSession = workflowSession;
        this.templateDesigner = null;

        this.toolbox = toolbox;

        this.image = document.getElementById("pdfImage");

        this.svg = document.getElementById("overlaySvg");

        this.renderer = new SvgRenderer(this.svg);

        this.previewRenderer = new PreviewRenderer(this.svg);

        this.registerSvgHandlers();
        this.updateHistoryUi();
    }

    /**
     * Wires the dedicated template designer.
     *
     * @param {TemplateDesigner} templateDesigner
     */
    setTemplateDesigner(templateDesigner) {
        this.templateDesigner = templateDesigner;
    }

    /**
     * Keeps the shared workflow session aligned with the current cells.
     */
    syncWorkflowCells() {
        this.workflowSession?.setCells(this.state.cells);
    }

    /**
     * Loads backend result.
     *
     * @param result
     * @param cells
     */
    loadResult(result, cells) {
        this.state.jobId = result.jobId;
        this.state.cells = cells;
        this.state.selectedCellId = null;
        this.state.mode = EditorModes.SELECT;
        this.history = new HistoryManager();
        this.workflowSession?.setJobId(result.jobId);
        this.workflowSession?.setPage(result.page);
        this.syncWorkflowCells();
        this.toolbox.setMode(EditorModes.SELECT);
        this.previewRenderer.clear();
        this.updateHistoryUi();

        this.image.src = `${BACKEND_URL}${result.imageUrl}`;

        this.image.onload = () => {

            this.state.pageWidth = this.image.naturalWidth;

            this.state.pageHeight = this.image.naturalHeight;

            this.renderer.updateViewBox(
                this.state.pageWidth,
                this.state.pageHeight
            );

            this.rerender();
        };
    }

    /**
     * Sets editor mode.
     *
     * @param mode
     */
    setMode(mode) {
        this.state.mode = mode;
        // TODO: Collapse editor toolbox when in mapping mode, but have small button to open toolbox again.
    }

    /**
     * Undo.
     */
    undo() {
        this.state.cells = this.history.undo(
            this.state.cells
        );
        this.syncWorkflowCells();

        this.rerender();
        this.updateHistoryUi();
    }

    /**
     * Redo.
     */
    redo() {
        this.state.cells = this.history.redo(
            this.state.cells
        );
        this.syncWorkflowCells();

        this.rerender();
        this.updateHistoryUi();
    }

    /**
     * Refresh editor UI.
     */
    rerender() {
        this.renderer.renderCells(
            this.state.cells,
            this.state.selectedCellId,
            this.templateDesigner?.getTemplate() ?? this.workflowSession?.getTemplate(),
            this.onCellClick.bind(this),
            this.onCellMove.bind(this),
            this.onCellLeave.bind(this)
        );

        this.toolbox.updateSelectionInfo(
            this.state.getSelectedCell()
        );
    }

    /**
     * Refresh undo/redo buttons.
     */
    updateHistoryUi() {
        this.toolbox.updateHistoryState(
            this.history.undoStack.length,
            this.history.redoStack.length
        );
    }

    /**
     * Handles cell clicks.
     *
     * @param cell
     * @param event
     */
    onCellClick(cell, event) {
        const point = this.renderer.svgMousePoint(event);

        switch (this.state.mode) {
            case EditorModes.SELECT:
                this.state.selectCell(cell.id);

                this.rerender();
                break;
            case EditorModes.DELETE:
                this.history.push(this.state.cells);
                this.state.cells = CellOperations.delete(
                    this.state.cells,
                    cell.id
                );
                this.syncWorkflowCells();

                this.rerender();
                this.updateHistoryUi();
                break;
            case EditorModes.SPLIT_VERTICAL:
                this.history.push(this.state.cells);
                this.state.cells = CellOperations.splitVertical(this.state.cells, cell, point.x);
                this.syncWorkflowCells();
                this.previewRenderer.clear();

                this.rerender();
                this.updateHistoryUi();
                break;
            case EditorModes.SPLIT_HORIZONTAL:
                this.history.push(this.state.cells);
                this.state.cells = CellOperations.splitHorizontal(this.state.cells, cell, point.y);
                this.syncWorkflowCells();
                this.previewRenderer.clear();

                this.rerender();
                this.updateHistoryUi();
                break;
            case EditorModes.MAPPING:
                this.templateDesigner?.assignCell(cell);

                this.rerender();
                break;
            case EditorModes.HEADER:
                this.history.push(this.state.cells);
                this.state.cells = CellOperations.toggleHeader(this.state.cells, cell.id);
                this.syncWorkflowCells();

                this.rerender();
                this.updateHistoryUi();
                break;
        }
    }

    /**
     * Split preview.
     *
     * @param cell
     * @param event
     */
    onCellMove(cell, event) {
        const point = this.renderer.svgMousePoint(event);

        if (this.state.mode === EditorModes.SPLIT_VERTICAL) {
            this.previewRenderer.showSplitPreview(
                EditorModes.SPLIT_VERTICAL,
                cell,
                point.x,
                point.y
            );
        }

        if (this.state.mode === EditorModes.SPLIT_HORIZONTAL) {
            this.previewRenderer.showSplitPreview(
                EditorModes.SPLIT_HORIZONTAL,
                cell,
                point.x,
                point.y
            );
        }
    }

    /**
     * Hide split preview.
     */
    onCellLeave() {
        this.previewRenderer.removeSplitPreview();
    }

    /**
     * Register SVG handlers.
     */
    registerSvgHandlers() {
        this.svg.addEventListener("mousedown", event =>
            this.onMouseDown(event)
        );

        this.svg.addEventListener("mousemove", event =>
            this.onMouseMove(event)
        );

        this.svg.addEventListener("mouseup", event =>
            this.onMouseUp(event)
        );
    }

    /**
     * Start drawing.
     */
    onMouseDown(event) {
        if (this.state.mode !== EditorModes.DRAW) {
            return;
        }

        const point = this.renderer.svgMousePoint(event);
        this.state.drawing = true;
        this.state.drawStartX = point.x;
        this.state.drawStartY = point.y;
    }

    /**
     * Draw preview rectangle.
     */
    onMouseMove(event) {
        if (!this.state.drawing) {
            return;
        }

        const point = this.renderer.svgMousePoint(event);

        this.previewRenderer.showDrawPreview(
            this.state.drawStartX,
            this.state.drawStartY,
            point.x,
            point.y
        );
    }

    /**
     * Finish drawing.
     */
    onMouseUp(event) {
        if (!this.state.drawing) {
            return;
        }

        this.state.drawing = false;

        const point = this.renderer.svgMousePoint(event);

        const x = Math.min(this.state.drawStartX, point.x);

        const y = Math.min(this.state.drawStartY, point.y);

        const width = Math.abs(point.x - this.state.drawStartX);

        const height = Math.abs(point.y - this.state.drawStartY);

        if (width < 10 || height < 10) {
            this.previewRenderer.clear();
            return;
        }

        this.history.push(this.state.cells);

        this.state.cells = CellOperations.create(
            this.state.cells,
            x,
            y,
            width,
            height
        );
        this.syncWorkflowCells();

        this.previewRenderer.clear();

        this.rerender();
        this.updateHistoryUi();
    }
}
