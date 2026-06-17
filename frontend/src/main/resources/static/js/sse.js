document.getElementById('uploadForm').addEventListener('submit', async function (e) {
    e.preventDefault(); // prevent page reload

    const fileInput = document.getElementById('file');
    const processOcrCheckbox = document.getElementById('processOcr');

    if (fileInput.files.length === 0) {
        showAlert('Please select a file.', 'danger');
        return;
    }

    const formData = new FormData();
    formData.append('file', fileInput.files[0]);
    formData.append('processOcr', processOcrCheckbox.checked);

    try {
        const response = await fetch(`${BACKEND_URL}/api/v1/upload`, {
            method: 'POST',
            body: formData
        });

        if (response.status !== 202) {
            const text = await response.text();
            showAlert('❌ Invalid input: ' + text, 'danger');
        } else {
            const uploadResult = await response.json();
            startJob(uploadResult.jobId);
        }
    } catch (error) {
        showAlert('❌ Network error: ' + error, 'danger');
    }


});

/**
 * Connect to sse in order to be informed once the processing made progress and updates are available
 * @param jobId the job assigned for this task
 */
function startJob(jobId) {
    document.getElementById("uploadForm").classList.add("d-none");
    document.getElementById("processingView").classList.remove("d-none");

    const eventSource =
        new EventSource(
            `${BACKEND_URL}/api/v1/upload/jobs/${jobId}/updates`
        );

    // TODO: Listen for different types of messages and display them differently
    // progress vs table detected

    eventSource.onmessage = event => {
        const data = JSON.parse(event.data);
        showPdfResult(data);
        document
            .getElementById("processingView")
            .classList.add("d-none");
        document
            .getElementById("viewerView")
            .classList.remove("d-none");
    };

    eventSource.onerror = error => {
        console.error(error);
        showAlert(
            "Connection to processing job lost",
            "danger"
        );
        eventSource.close();
    };
}

/*****************************************************************

 * EDITOR STATE
 *****************************************************************/

const editorState = {
    mode: "select",

    cells: [],

    selectedCellId: null,

    history: [],
    redoStack: [],

    pageWidth: 0,
    pageHeight: 0,

    drawing: false,

    drawStartX: null,
    drawStartY: null
};

/*****************************************************************
 * MODE SWITCHING
 *****************************************************************/

function setMode(mode) {
    editorState.mode = mode;

    document
        .querySelectorAll(".card button")
        .forEach(btn => btn.classList.remove("active"));

    switch (mode) {

        case "select":
            document
                .getElementById("selectModeBtn")
                .classList.add("active");
            break;

        case "splitHorizontal":
            document
                .getElementById("splitHorizontalBtn")
                .classList.add("active");
            break;

        case "splitVertical":
            document
                .getElementById("splitVerticalBtn")
                .classList.add("active");
            break;

        case "delete":
            document
                .getElementById("deleteBtn")
                .classList.add("active");
            break;

        case "mapping":
            document
                .getElementById("mappingBtn")
                .classList.add("active");
            break;
    }

}

document
    .getElementById("selectModeBtn")
    .addEventListener("click", () => setMode("select"));

document
    .getElementById("splitHorizontalBtn")
    .addEventListener("click", () => setMode("splitHorizontal"));

document
    .getElementById("splitVerticalBtn")
    .addEventListener("click", () => setMode("splitVertical"));

document
    .getElementById("drawBtn")
    .addEventListener("click", () => setMode("draw"));

document
    .getElementById("deleteBtn")
    .addEventListener("click", () => setMode("delete"));

document
    .getElementById("mappingBtn")
    .addEventListener("click", () => setMode("mapping"));

/*****************************************************************
 * HISTORY
 *****************************************************************/

function pushHistory() {

    editorState.history.push(
        structuredClone(editorState.cells)
    );
    editorState.redoStack = [];
}

document
    .getElementById("undoBtn")
    .addEventListener("click", () => {

        if (editorState.history.length === 0)
            return;

        editorState.redoStack.push(
            structuredClone(editorState.cells)
        );

        editorState.cells = editorState.history.pop();

        rerender();
    });

document
    .getElementById("redoBtn")
    .addEventListener("click", () => {

        if (editorState.redoStack.length === 0)
            return;

        editorState.history.push(
            structuredClone(editorState.cells)
        );

        editorState.cells = editorState.redoStack.pop();

        rerender();
    });

/*****************************************************************
 * DISPLAY RESULT
 *****************************************************************/

function showPdfResult(result) {
    editorState.cells = [];
    result.cells.forEach((row, rowIndex) => {
        row.forEach((cell, colIndex) => {
            editorState.cells.push({
                id: crypto.randomUUID(),
                row: rowIndex,
                column: colIndex,
                x: cell.x,
                y: cell.y,
                width: cell.width,
                height: cell.height
            });
        });
    });

    const image =
        document.getElementById("pdfImage");

    image.src = `${BACKEND_URL}${result.imageUrl}`;

    image.onload = () => {
        editorState.pageWidth = image.naturalWidth;
        editorState.pageHeight = image.naturalHeight;
        rerender();
    };
}

/*****************************************************************
 * RENDER
 *****************************************************************/

function rerender() {
    renderCells(
        editorState.cells,
        editorState.pageWidth,
        editorState.pageHeight
    );

    initializeSvgDrawing();
}

function initializeSvgDrawing() {

    const svg =
        document.getElementById("overlaySvg");

    svg.onmousedown = startDrawing;
    svg.onmousemove = drawingMove;
    svg.onmouseup = finishDrawing;
}

function renderCells(cells, pageWidth, pageHeight) {
    const svg =
        document.getElementById("overlaySvg");
    svg.innerHTML = "";
    svg.setAttribute(
        "viewBox",
        `0 0 ${pageWidth} ${pageHeight}`
    );

    cells.forEach(cell => {
        const rect =
            document.createElementNS(
                "http://www.w3.org/2000/svg",
                "rect"
            );

        rect.setAttribute("x", cell.x);
        rect.setAttribute("y", cell.y);
        rect.setAttribute("width", cell.width);
        rect.setAttribute("height", cell.height);

        rect.setAttribute(
            "stroke",
            "#0d6efd"
        );

        rect.setAttribute(
            "fill",
            cell.id === editorState.selectedCellId
                ? "rgba(25,135,84,0.45)"
                : "rgba(0,123,255,0.25)"
        );

        rect.style.cursor = "pointer";

        attachCellHandlers(
            rect,
            cell,
            svg
        );

        svg.appendChild(rect);
    });

}

/*****************************************************************
 * CELL EVENTS
 *****************************************************************/

function attachCellHandlers(
    rect,
    cell,
    svg
) {

    rect.addEventListener(
        "click",
        event => {
            switch (editorState.mode) {
                case "select":
                    selectCell(cell);
                    break;
                case "delete":
                    deleteCell(cell);
                    break;
                case "splitVertical":
                    splitVerticalAt(
                        cell,
                        svgMousePoint(
                            svg,
                            event
                        ).x
                    );
                    break;
                case "splitHorizontal":
                    splitHorizontalAt(
                        cell,
                        svgMousePoint(
                            svg,
                            event
                        ).y
                    );
                    break;
                case "mapping":
                    console.log(
                        "Mapping cell",
                        cell
                    );
                    break;
            }
        }
    );

    rect.addEventListener(
        "mousemove",
        event => {
            if (editorState.mode !== "splitVertical" && editorState.mode !== "splitHorizontal") {
                return;
            }
            renderPreviewLine(
                svg,
                cell,
                event
            );
        }
    );

    rect.addEventListener(
        "mouseleave",
        () => removePreviewLine(svg)
    );
}

/****************************************************************
 * DRAWING
 *****************************************************************/
function startDrawing(event) {

    if (editorState.mode !== "draw")
        return;

    const svg =
        document.getElementById("overlaySvg");

    const point =
        svgMousePoint(svg, event);

    editorState.drawing = true;

    editorState.drawStartX = point.x;
    editorState.drawStartY = point.y;
}

function drawingMove(event) {

    const svg =
        document.getElementById("overlaySvg");

    const point =
        svgMousePoint(svg, event);

    //
    // split preview
    //
    if (
        editorState.mode === "splitVertical" ||
        editorState.mode === "splitHorizontal"
    ) {
        renderSplitPreview(
            point
        );
    }

    //
    // draw preview
    //
    if (
        editorState.mode === "draw" &&
        editorState.drawing
    ) {

        renderDrawPreview(
            svg,
            point
        );
    }
}

function renderDrawPreview(
    svg,
    point
) {

    removeDrawPreview();

    const rect =
        document.createElementNS(
            "http://www.w3.org/2000/svg",
            "rect"
        );

    rect.id = "drawPreview";

    const x =
        Math.min(
            editorState.drawStartX,
            point.x
        );

    const y =
        Math.min(
            editorState.drawStartY,
            point.y
        );

    const width =
        Math.abs(
            point.x -
            editorState.drawStartX
        );

    const height =
        Math.abs(
            point.y -
            editorState.drawStartY
        );

    rect.setAttribute("x", x);
    rect.setAttribute("y", y);

    rect.setAttribute(
        "width",
        width
    );

    rect.setAttribute(
        "height",
        height
    );

    rect.setAttribute(
        "fill",
        "rgba(25,135,84,0.2)"
    );

    rect.setAttribute(
        "stroke",
        "green"
    );

    rect.setAttribute(
        "stroke-width",
        "2"
    );

    svg.appendChild(rect);
}

function finishDrawing(event) {

    if (
        editorState.mode !== "draw" ||
        !editorState.drawing
    ) {
        return;
    }

    editorState.drawing = false;

    const svg =
        document.getElementById("overlaySvg");

    const point =
        svgMousePoint(svg, event);

    const x =
        Math.min(
            editorState.drawStartX,
            point.x
        );

    const y =
        Math.min(
            editorState.drawStartY,
            point.y
        );

    const width =
        Math.abs(
            point.x -
            editorState.drawStartX
        );

    const height =
        Math.abs(
            point.y -
            editorState.drawStartY
        );

    if (
        width < 10 ||
        height < 10
    ) {
        removeDrawPreview();
        return;
    }

    pushHistory();

    editorState.cells.push({

        id: crypto.randomUUID(),

        row: -1,
        column: -1,

        x,
        y,

        width,
        height
    });

    removeDrawPreview();

    rerender();
}

function removeDrawPreview() {

    const existing =
        document.getElementById(
            "drawPreview"
        );

    if (existing)
        existing.remove();
}


/*****************************************************************
 * SELECTION
 *****************************************************************/

function selectCell(cell) {
    editorState.selectedCellId = cell.id;
    document
        .getElementById("selectionInfo")
        .innerHTML = `
                    Cell<br>
                    x=${Math.round(cell.x)}<br>
                        y=${Math.round(cell.y)}<br>
                        width=${Math.round(cell.width)}<br>
                        height=${Math.round(cell.height)}
                        `;

    rerender();
}

/*****************************************************************
 * DELETE
 *****************************************************************/

function deleteCell(cell) {
    pushHistory();
    editorState.cells =
        editorState.cells.filter(
            c => c.id !== cell.id
        );
    rerender();
}

/*****************************************************************
 * SPLIT
 *****************************************************************/

function splitVerticalAt(cell, splitX) {
    const localX =
        splitX - cell.x;
    if (
        localX < 10 ||
        localX > cell.width - 10
    ) {
        return;
    }

    pushHistory();

    const left = {
        ...cell,
        id: crypto.randomUUID(),
        width: localX
    };

    const right = {
        ...cell,
        id: crypto.randomUUID(),
        x: splitX,
        width: cell.width - localX
    };

    replaceCell(
        cell.id,
        [left, right]
    );

}

function splitHorizontalAt(cell, splitY) {
    const localY =
        splitY - cell.y;

    if (
        localY < 10 ||
        localY > cell.height - 10
    ) {
        return;
    }

    pushHistory();

    const top = {
        ...cell,
        id: crypto.randomUUID(),
        height: localY
    };

    const bottom = {
        ...cell,
        id: crypto.randomUUID(),
        y: splitY,
        height: cell.height - localY
    };

    replaceCell(
        cell.id,
        [top, bottom]
    );
}

function replaceCell(
    cellId,
    replacements
) {

    const index =
        editorState.cells.findIndex(
            c => c.id === cellId
        );

    if (index === -1)
        return;

    editorState.cells.splice(
        index,
        1,
        ...replacements
    );

    rerender();

}

/*****************************************************************
 * PREVIEW LINE
 *****************************************************************/

function renderPreviewLine(
    svg,
    cell,
    event
) {

    removePreviewLine(svg);

    const point =
        svgMousePoint(svg, event);

    const line =
        document.createElementNS(
            "http://www.w3.org/2000/svg",
            "line"
        );

    line.id = "splitPreview";

    if (
        editorState.mode ===
        "splitVertical"
    ) {

        line.setAttribute(
            "x1",
            point.x
        );

        line.setAttribute(
            "x2",
            point.x
        );

        line.setAttribute(
            "y1",
            cell.y
        );

        line.setAttribute(
            "y2",
            cell.y + cell.height
        );
    } else {

        line.setAttribute(
            "x1",
            cell.x
        );

        line.setAttribute(
            "x2",
            cell.x + cell.width
        );

        line.setAttribute(
            "y1",
            point.y
        );

        line.setAttribute(
            "y2",
            point.y
        );
    }

    line.setAttribute(
        "stroke",
        "red"
    );

    line.setAttribute(
        "stroke-width",
        "2"
    );

    svg.appendChild(line);

}

function removePreviewLine(svg) {

    const existing =
        document.getElementById(
            "splitPreview"
        );

    if (existing)
        existing.remove();
}

/*****************************************************************
 * SVG COORDS
 *****************************************************************/

function svgMousePoint(
    svg,
    event
) {
    const pt =
        svg.createSVGPoint();
    pt.x = event.clientX;
    pt.y = event.clientY;
    return pt.matrixTransform(
        svg
            .getScreenCTM()
            .inverse()
    );
}


function showAlert(message, type) {
    const alertPlaceholder = document.getElementById('alertPlaceholder');
    alertPlaceholder.innerHTML = `
            <div class="alert alert-${type}" role="alert">
                ${message}
            </div>
        `;
}
