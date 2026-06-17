import {EditorModes} from "../editor/EditorModes.js";

/**
 * Handles all toolbox interactions.
 */
export class Toolbox {

    /**
     * @param {Object} options
     */
    constructor(options) {
        this.onModeChange = options.onModeChange;
        this.onUndo = options.onUndo;
        this.onRedo = options.onRedo;

        this.selectionInfo = document.getElementById("selectionInfo");
        this.buttons = {
            select: document.getElementById("selectModeBtn"),
            splitVertical: document.getElementById("splitVerticalBtn"),
            splitHorizontal: document.getElementById("splitHorizontalBtn"),
            draw: document.getElementById("drawBtn"),
            delete: document.getElementById("deleteBtn"),
            mapping: document.getElementById("mappingBtn"),
            undo: document.getElementById("undoBtn"),
            redo: document.getElementById("redoBtn")
        };
    }

    /**
     * Initializes all event listeners.
     */
    initialize() {
        this.buttons.select?.addEventListener("click", () =>
            this.changeMode(EditorModes.SELECT)
        );

        this.buttons.splitVertical?.addEventListener("click", () =>
            this.changeMode(EditorModes.SPLIT_VERTICAL)
        );

        this.buttons.splitHorizontal?.addEventListener("click", () =>
            this.changeMode(EditorModes.SPLIT_HORIZONTAL)
        );

        this.buttons.draw?.addEventListener("click", () =>
            this.changeMode(EditorModes.DRAW)
        );

        this.buttons.delete?.addEventListener("click", () =>
            this.changeMode(EditorModes.DELETE)
        );

        this.buttons.mapping?.addEventListener("click", () =>
            this.changeMode(EditorModes.MAPPING)
        );

        this.buttons.undo?.addEventListener("click", () =>
            this.onUndo?.()
        );

        this.buttons.redo?.addEventListener("click", () =>
            this.onRedo?.()
        );
    }

    /**
     * Updates active mode.
     *
     * @param {string} mode
     */
    setMode(mode) {
        Object.values(this.buttons).forEach(button => {
            button?.classList.remove("active");
        });

        switch (mode) {
            case EditorModes.SELECT:
                this.buttons.select?.classList.add("active");
                break;
            case EditorModes.SPLIT_VERTICAL:
                this.buttons.splitVertical?.classList.add("active");
                break;
            case EditorModes.SPLIT_HORIZONTAL:
                this.buttons.splitHorizontal?.classList.add("active");
                break;
            case EditorModes.DRAW:
                this.buttons.draw?.classList.add("active");
                break;
            case EditorModes.DELETE:
                this.buttons.delete?.classList.add("active");
                break;
            case EditorModes.MAPPING:
                this.buttons.mapping?.classList.add("active");
                break;
        }
    }

    /**
     * Updates selected cell display.
     *
     * @param {Object|null} cell
     */
    updateSelectionInfo(cell) {
        if (!cell) {
            this.selectionInfo.innerHTML = "No cell selected";
            return;
        }

        this.selectionInfo.innerHTML = `
            Cell<br>
            x=${Math.round(cell.x)}<br>
            y=${Math.round(cell.y)}<br>
            width=${Math.round(cell.width)}<br>
            height=${Math.round(cell.height)}
        `;
    }

    /**
     * Handles mode changes.
     *
     * @param {string} mode
     */
    changeMode(mode) {
        this.setMode(mode);
        this.onModeChange?.(mode);
    }

    /**
     * Enables undo button.
     *
     * @param {boolean} enabled
     */
    setUndoEnabled(enabled) {
        this.buttons.undo.disabled = !enabled;
    }

    /**
     * Enables redo button.
     *
     * @param {boolean} enabled
     */
    setRedoEnabled(enabled) {
        this.buttons.redo.disabled = !enabled;
    }

    /**
     * Updates history controls.
     *
     * @param {number} undoCount
     * @param {number} redoCount
     */
    updateHistoryState(undoCount, redoCount) {
        this.setUndoEnabled(undoCount > 0);
        this.setRedoEnabled(redoCount > 0);
    }
}
