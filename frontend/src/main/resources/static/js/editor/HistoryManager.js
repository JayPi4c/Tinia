/**
 * Manages undo / redo operations.
 */
export class HistoryManager {

    constructor() {

        /**
         * Undo stack.
         *
         * @type {Array}
         */
        this.undoStack = [];

        /**
         * Redo stack.
         *
         * @type {Array}
         */
        this.redoStack = [];
    }

    /**
     * Pushes a new state into history.
     *
     * @param {Array} cells
     */
    push(cells) {
        this.undoStack.push(structuredClone(cells));
        this.redoStack = [];
    }

    /**
     * Performs undo.
     *
     * @param {Array} currentCells
     * @returns {Array}
     */
    undo(currentCells) {
        if (this.undoStack.length === 0) {
            return currentCells;
        }
        this.redoStack.push(structuredClone(currentCells));
        return this.undoStack.pop();
    }

    /**
     * Performs redo.
     *
     * @param {Array} currentCells
     * @returns {Array}
     */
    redo(currentCells) {
        if (this.redoStack.length === 0) {
            return currentCells;
        }
        this.undoStack.push(structuredClone(currentCells));
        return this.redoStack.pop();
    }

    /**
     * Clears history.
     */
    clear() {
        this.undoStack = [];
        this.redoStack = [];
    }
}
