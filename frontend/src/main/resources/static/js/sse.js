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

const selectedCells = new Set();

/**
 * Displays the processed page in the frontend and renders the cells on top of the image once it is completely loaded.
 * @param result the sse event data
 */
function showPdfResult(result) {
    const image = document.getElementById("pdfImage");
    image.src = `${BACKEND_URL}${result.imageUrl}`;
    image.onload = () => {
        renderCells(
            result.cells,
            image.naturalWidth,
            image.naturalHeight
        );
    };
}

/**
 * Function to render the cells on top of the PDF. This is done via a overlay which has also a click listener in order
 * for the user to be able to click on the individual cells.
 *
 * This allows the user to interact with the intermediate result and approve the current state.
 * @param cells the identified cells by the backend
 * @param pageWidth
 * @param pageHeight
 */
function renderCells(cells, pageWidth, pageHeight) {
    const svg = document.getElementById("overlaySvg");
    svg.innerHTML = "";
    svg.setAttribute(
        "viewBox",
        `0 0 ${pageWidth} ${pageHeight}`
    );
    cells.forEach((row, rowIndex) => {
        row.forEach((cell, colIndex) => {
            const rect = document.createElementNS(
                "http://www.w3.org/2000/svg",
                "rect"
            );

            rect.setAttribute("x", cell.x);
            rect.setAttribute("y", cell.y);
            rect.setAttribute("width", cell.width);
            rect.setAttribute("height", cell.height);
            rect.setAttribute(
                "fill",
                "rgba(0,123,255,0.25)"
            );
            rect.setAttribute(
                "stroke",
                "#0d6efd"
            );
            rect.style.cursor = "pointer";
            const key = `${rowIndex}-${colIndex}`;
            rect.addEventListener("click", () => {
                if (selectedCells.has(key)) {
                    selectedCells.delete(key);
                    rect.setAttribute(
                        "fill",
                        "rgba(0,123,255,0.25)"
                    );
                } else {
                    selectedCells.add(key);
                    rect.setAttribute(
                        "fill",
                        "rgba(25,135,84,0.45)"
                    );
                }
                console.log([...selectedCells]);
            });
            svg.appendChild(rect);
        });
    });
}

function showAlert(message, type) {
    const alertPlaceholder = document.getElementById('alertPlaceholder');
    alertPlaceholder.innerHTML = `
            <div class="alert alert-${type}" role="alert">
                ${message}
            </div>
        `;
}
