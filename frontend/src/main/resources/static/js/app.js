import {UploadApi} from "./api/UploadApi.js";
import {JobClient} from "./api/JobClient.js";
import {Toolbox} from "./ui/Toolbox.js";
import {AlertService} from "./ui/AlertService.js";
import {PdfEditor} from "./editor/PdfEditor.js";
import {CellOperations} from "./editor/CellOperations.js";

const alertService = new AlertService();
const uploadApi = new UploadApi(BACKEND_URL);
const jobClient = new JobClient(BACKEND_URL);

let editor;

const toolbox =
    new Toolbox({
        onModeChange: mode => editor.setMode(mode),
        onUndo: () => editor.undo(),
        onRedo: () => editor.redo()
    });

toolbox.initialize();

editor = new PdfEditor(toolbox);

document.getElementById("uploadForm")
    .addEventListener(
        "submit",
        async event => {
            event.preventDefault();
            const fileInput = document.getElementById("file");
            const processOcr = document.getElementById("processOcr");

            if (fileInput.files.length === 0) {
                alertService.error("Please select a file.");
                return;
            }

            try {
                const result = await uploadApi.upload(fileInput.files[0], processOcr.checked);
                startJob(result.jobId);
            } catch (error) {
                alertService.error(error.message);
            }
        }
    );

function startJob(jobId) {
    document.getElementById("uploadForm")
        .classList.add(
        "d-none"
    );

    document.getElementById("processingView")
        .classList.remove(
        "d-none"
    );

    jobClient.connect(jobId, {
            onResult: result => {
                const cells = CellOperations.fromBackend(result.cells);
                editor.loadResult(result, cells);

                document.getElementById("processingView")
                    .classList.add(
                    "d-none"
                );

                document.getElementById("viewerView")
                    .classList.remove(
                    "d-none"
                );
            },
            onError: () => {
                alertService.error("Connection to processing job lost");
            }
        }
    );
}
