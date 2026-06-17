/**
 * Represents a single editable cell on the PDF page.
 */
export class Cell {

    /**
     * @param {Object} params
     * @param {string} params.id
     * @param {number} params.x
     * @param {number} params.y
     * @param {number} params.width
     * @param {number} params.height
     * @param {number} [params.row=-1]
     * @param {number} [params.column=-1]
     */
    constructor({
                    id,
                    x,
                    y,
                    width,
                    height,
                    row = -1,
                    column = -1,
                    type = "DATA"
                }) {

        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.row = row;
        this.column = column;

        /**
         * HEADER
         * DATA
         */
        this.type = type;
    }

    /**
     * Creates a deep copy of the cell.
     *
     * @returns {Cell}
     */
    clone() {

        return new Cell({
            id: crypto.randomUUID(),
            x: this.x,
            y: this.y,
            width: this.width,
            height: this.height,
            row: this.row,
            column: this.column,
            type: this.type
        });
    }
}
