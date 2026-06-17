import {Cell} from "../models/Cell.js";

/**
 * Stateless utility methods for modifying cells.
 */
export class CellOperations {

    /**
     * Deletes a cell.
     *
     * @param {Array<Cell>} cells
     * @param {string} cellId
     * @returns {Array<Cell>}
     */
    static delete(cells, cellId) {
        return cells.filter(cell => cell.id !== cellId);
    }

    /**
     * Creates a new cell.
     *
     * @param {Array<Cell>} cells
     * @param {number} x
     * @param {number} y
     * @param {number} width
     * @param {number} height
     * @returns {Array<Cell>}
     */
    static create(cells, x, y, width, height) {
        const newCell = new Cell({
            id: crypto.randomUUID(),

            x,
            y,

            width,
            height,

            row: -1,
            column: -1
        });

        return [...cells, newCell];
    }

    /**
     * Splits a cell vertically.
     *
     * @param {Array<Cell>} cells
     * @param {Cell} cell
     * @param {number} splitX
     * @returns {Array<Cell>}
     */
    static splitVertical(cells, cell, splitX) {
        const localX = splitX - cell.x;

        if (localX < 10 || localX > cell.width - 10) {
            return cells;
        }

        const left = new Cell({
            ...cell,
            id: crypto.randomUUID(),
            width: localX
        });

        const right = new Cell({
            ...cell,
            id: crypto.randomUUID(),
            x: splitX,
            width: cell.width - localX
        });

        return this.replace(cells, cell.id, [left, right]);
    }

    /**
     * Splits a cell horizontally.
     *
     * @param {Array<Cell>} cells
     * @param {Cell} cell
     * @param {number} splitY
     * @returns {Array<Cell>}
     */
    static splitHorizontal(cells, cell, splitY) {
        const localY = splitY - cell.y;

        if (localY < 10 || localY > cell.height - 10) {
            return cells;
        }

        const top = new Cell({
            ...cell,
            id: crypto.randomUUID(),
            height: localY
        });

        const bottom = new Cell({
            ...cell,
            id: crypto.randomUUID(),
            y: splitY,
            height: cell.height - localY
        });

        return this.replace(cells, cell.id, [top, bottom]);
    }

    /**
     * Replaces a cell with one or multiple cells.
     *
     * @param {Array<Cell>} cells
     * @param {string} cellId
     * @param {Array<Cell>} replacements
     * @returns {Array<Cell>}
     */
    static replace(cells, cellId, replacements) {
        const index = cells.findIndex(cell => cell.id === cellId);

        if (index === -1) {
            return cells;
        }

        const result = [...cells];

        result.splice(index, 1, ...replacements);

        return result;
    }

    /**
     * Converts backend result into editor cells.
     *
     * @param {Array} rows
     * @returns {Array<Cell>}
     */
    static fromBackend(rows) {
        const result = [];

        rows.forEach((row, rowIndex) => {
                row.forEach((cell, columnIndex) => {
                        result.push(new Cell({
                                id: crypto.randomUUID(),

                                row: rowIndex,
                                column: columnIndex,

                                x: cell.x,
                                y: cell.y,

                                width: cell.width,
                                height: cell.height
                            })
                        );
                    }
                );
            }
        );

        return result;
    }
}
