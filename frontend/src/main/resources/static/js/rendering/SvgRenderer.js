/**
 * Responsible for rendering all persistent SVG content.
 *
 * This class only visualizes cells.
 * It does not perform editor actions.
 */
export class SvgRenderer {

    /**
     * @param {SVGSVGElement} svg
     */
    constructor(svg) {
        this.svg = svg;
    }

    /**
     * Clears SVG.
     */
    clear() {
        this.svg.innerHTML = "";
    }

    /**
     * Updates SVG viewbox.
     *
     * @param {number} width
     * @param {number} height
     */
    updateViewBox(width, height) {
        this.svg.setAttribute("viewBox", `0 0 ${width} ${height}`);
    }

    /**
     * Renders all cells.
     *
     * @param {Array} cells
     * @param {string|null} selectedCellId
     * @param {Function} clickHandler
     * @param {Function} moveHandler
     * @param {Function} leaveHandler
     */
    renderCells(cells, selectedCellId, clickHandler, moveHandler, leaveHandler) {
        this.clear();
        cells.forEach(cell => {
            const rect = this.createCellRect(cell, selectedCellId);

            rect.addEventListener("click", event =>
                clickHandler(cell, event)
            );

            rect.addEventListener("mousemove", event =>
                moveHandler(cell, event)
            );

            rect.addEventListener("mouseleave", () =>
                leaveHandler(cell)
            );

            this.svg.appendChild(rect);
        });
    }

    /**
     * Creates SVG rect.
     *
     * @param {Object} cell
     * @param {string|null} selectedCellId
     * @returns {SVGRectElement}
     */
    createCellRect(cell, selectedCellId) {
        const rect = document.createElementNS(
            "http://www.w3.org/2000/svg",
            "rect"
        );

        rect.setAttribute("x", cell.x);
        rect.setAttribute("y", cell.y);
        rect.setAttribute("width", cell.width);
        rect.setAttribute("height", cell.height);

        rect.setAttribute("stroke", "#0d6efd");
        rect.setAttribute("stroke-width", "1");

        let fill = "rgba(0,123,255,0.25)";
        if (cell.id === selectedCellId) {
            fill = "rgba(25,135,84,0.45)";
        } else if (cell.type === "HEADER") {
            fill = "rgba(255,193,7,0.45)";
        } else if (cell.mapping) {
            fill = "rgba(13,202,240,0.45)";
        }
        rect.setAttribute("fill", fill);

        rect.style.cursor = "pointer";

        return rect;
    }

    /**
     * Converts browser coordinates
     * into SVG coordinates.
     *
     * @param {MouseEvent} event
     * @returns {{x:number,y:number}}
     */
    svgMousePoint(event) {
        const pt = this.svg.createSVGPoint();
        pt.x = event.clientX;
        pt.y = event.clientY;

        return pt.matrixTransform(
            this.svg
                .getScreenCTM()
                .inverse()
        );
    }

    /**
     * Returns SVG element.
     *
     * Useful for registering
     * global mouse handlers.
     *
     * @returns {SVGSVGElement}
     */
    getSvg() {
        return this.svg;
    }
}
