import {UploadApi} from "./api/UploadApi.js";
import {JobClient} from "./api/JobClient.js";
import {Toolbox} from "./ui/Toolbox.js";
import {AlertService} from "./ui/AlertService.js";
import {PdfEditor} from "./editor/PdfEditor.js";
import {CellOperations} from "./editor/CellOperations.js";
import {ProcessingSession} from "./workflow/ProcessingSession.js";
import {TemplateDesigner} from "./mapping/TemplateDesigner.js";

const alertService = new AlertService();
const uploadApi = new UploadApi(BACKEND_URL);
const jobClient = new JobClient(BACKEND_URL);
const workflowSession = new ProcessingSession();

let editor;
let templateDesigner;

const toolbox =
    new Toolbox({
        onModeChange: mode => editor?.setMode(mode),
        onUndo: () => editor.undo(),
        onRedo: () => editor.redo()
    });

toolbox.initialize();

editor = new PdfEditor(toolbox, workflowSession);
templateDesigner = new TemplateDesigner(uploadApi, workflowSession);
editor.setTemplateDesigner(templateDesigner);

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
    jobClient.disconnect();

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
                templateDesigner.reset();
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
