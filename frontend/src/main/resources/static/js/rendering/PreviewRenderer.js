/**
 * Responsible for rendering temporary SVG preview elements.
 *
 * Examples:
 * - Split preview line
 * - Draw preview rectangle
 */
export class PreviewRenderer {

    /**
     * @param {SVGSVGElement} svg
     */
    constructor(svg) {
        this.svg = svg;
    }

    /**
     * Removes all preview elements.
     */
    clear() {
        this.removeSplitPreview();
        this.removeDrawPreview();
    }

    /**
     * Renders split preview.
     *
     * @param {"splitVertical"|"splitHorizontal"} mode
     * @param {Object} cell
     * @param {number} x
     * @param {number} y
     */
    showSplitPreview(mode, cell, x, y) {
        this.removeSplitPreview();

        const line = document.createElementNS(
            "http://www.w3.org/2000/svg",
            "line"
        );

        line.id = "splitPreview";

        if (mode === "splitVertical") {
            line.setAttribute("x1", x);
            line.setAttribute("x2", x);
            line.setAttribute("y1", cell.y);
            line.setAttribute("y2", cell.y + cell.height);
        } else {
            line.setAttribute("x1", cell.x);
            line.setAttribute("x2", cell.x + cell.width);
            line.setAttribute("y1", y);
            line.setAttribute("y2", y);
        }

        line.setAttribute("stroke", "red");
        line.setAttribute("stroke-width", "2");
        line.setAttribute("pointer-events", "none");

        this.svg.appendChild(line);
    }

    /**
     * Removes split preview.
     */
    removeSplitPreview() {
        const existing = document.getElementById("splitPreview");

        if (existing) {
            existing.remove();
        }
    }

    /**
     * Draw rectangle preview.
     *
     * @param {number} startX
     * @param {number} startY
     * @param {number} currentX
     * @param {number} currentY
     */
    showDrawPreview(startX, startY, currentX, currentY) {
        this.removeDrawPreview();

        const rect = document.createElementNS(
            "http://www.w3.org/2000/svg",
            "rect"
        );

        rect.id = "drawPreview";

        const x = Math.min(startX, currentX);
        const y = Math.min(startY, currentY);
        const width = Math.abs(currentX - startX);
        const height = Math.abs(currentY - startY);

        rect.setAttribute("x", x);
        rect.setAttribute("y", y);
        rect.setAttribute("width", width);
        rect.setAttribute("height", height);

        rect.setAttribute("fill", "rgba(25,135,84,0.20)");
        rect.setAttribute("stroke", "green");
        rect.setAttribute("stroke-width", "2");
        rect.setAttribute("pointer-events", "none");

        this.svg.appendChild(rect);
    }

    /**
     * Removes draw preview.
     */
    removeDrawPreview() {
        const existing = document.getElementById("drawPreview");

        if (existing) {
            existing.remove();
        }
    }
}
