import {MappingState} from "./MappingState.js";

import {MappingPanel} from "./MappingPanel.js";

import {MappingToolbox} from "./MappingToolbox.js";

/**
 * Coordinates mapping workflow.
 */
export class TemplateDesigner {

    /**
     * @param {UploadApi} uploadApi
     * @param {ProcessingSession} workflowSession
     */
    constructor(uploadApi, workflowSession) {
        this.uploadApi = uploadApi;
        this.workflowSession = workflowSession;

        this.state = new MappingState();
        this.panel = new MappingPanel("sidePanel");

        this.workflowSession?.setTemplate(this.state.template);

        this.toolbox = new MappingToolbox(this.panel, this.state, {
                onTargetSelected: field => this.selectBmpField(field),
                onCustomFieldCreated: name => this.createCustomField(name),
                onCustomFieldRemoved: id => this.removeCustomField(id),
                onContinue: () => this.continueProcessing()
            }
        );

        this.toolbox.refresh();
    }

    /**
     * Select BMP target.
     */
    selectBmpField(field) {
        this.state.selectTarget(field);
        this.toolbox.refresh();
    }

    /**
     * Create custom field.
     */
    createCustomField(name) {
        this.state.template.addCustomField(
            name,
            []
        );

        this.toolbox.refresh();
    }

    /**
     * Remove custom field.
     */
    removeCustomField(id) {
        this.state.template.removeCustomField(id);
        this.toolbox.refresh();
    }

    /**
     * Resets the mapping UI for a new document.
     */
    reset() {
        this.state = new MappingState();
        this.workflowSession?.setTemplate(this.state.template);
        this.toolbox.state = this.state;
        this.toolbox.refresh();
    }

    /**
     * Submits the current workflow state to the backend.
     */
    async continueProcessing() {
        const {jobId, page, cells, template} = this.workflowSession?.getContinuePayload?.() ?? {
            jobId: "",
            page: -1,
            cells: [],
            template: this.state.template
        };

        if (!jobId) {
            alert("No processing job is active yet.");
            return;
        }

        if (cells.length === 0) {
            alert("No cells available to continue with.");
            return;
        }

        await this.uploadApi.continueProcessing(jobId, page, cells, template);
    }

    /**
     * Assigns a header cell
     * to the currently selected
     * target field.
     *
     * @param {Object} cell
     */
    assignCell(cell) {
        if (!this.state.selectedTarget) {
            return;
        }

        if (cell.type !== "HEADER") {
            alert("Only header cells can be mapped.");
            return;
        }

        this.state.template.removeCellMapping(cell.id);
        this.state.template.assignBmpField(this.state.selectedTarget, cell.id);
        this.toolbox.refresh();
    }

    /**
     * Returns current template.
     */
    getTemplate() {
        return this.state.template;
    }
}
