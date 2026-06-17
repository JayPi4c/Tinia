import {EditorModes} from "./EditorModes.js";

/**
 * Central editor state.
 *
 * Contains only mutable application data.
 */
export class EditorState {

    constructor() {

        /**
         * Current editor mode.
         *
         * @type {string}
         */
        this.mode = EditorModes.SELECT;

        /**
         * Current cells.
         *
         * @type {Array}
         */
        this.cells = [];

        /**
         * Currently selected cell id.
         *
         * @type {string|null}
         */
        this.selectedCellId = null;

        /**
         * PDF page dimensions.
         *
         * @type {number}
         */
        this.pageWidth = 0;

        /**
         * PDF page dimensions.
         *
         * @type {number}
         */
        this.pageHeight = 0;

        /**
         * Drawing state.
         *
         * @type {boolean}
         */
        this.drawing = false;

        /**
         * Drawing start position.
         */
        this.drawStartX = null;
        this.drawStartY = null;
    }

    /**
     * Clears current selection.
     */
    clearSelection() {
        this.selectedCellId = null;
    }

    /**
     * Selects a cell.
     *
     * @param {string} cellId
     */
    selectCell(cellId) {
        this.selectedCellId = cellId;
    }

    /**
     * Returns selected cell.
     *
     * @returns {*|null}
     */
    getSelectedCell() {
        return this.cells.find(
            cell => cell.id === this.selectedCellId
        ) || null;
    }
}
