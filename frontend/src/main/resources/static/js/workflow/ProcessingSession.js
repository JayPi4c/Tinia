import {TemplateDefinition} from "../mapping/TemplateDefinition.js";

/**
 * Shared runtime state for one upload/processing session.
 *
 * The editor updates cells here and the mapping UI updates the template here,
 * so the continue request can read one coherent snapshot.
 */
export class ProcessingSession {

    constructor() {
        this.jobId = "";
        this.page = -1;
        this.cells = [];
        this.template = new TemplateDefinition();
    }

    setPage(page) {
        this.page = page ?? -1;
    }

    setJobId(jobId) {
        this.jobId = jobId ?? "";
    }

    setCells(cells) {
        this.cells = [...(cells ?? [])];
    }

    setTemplate(template) {
        this.template = template;
    }

    getTemplate() {
        return this.template;
    }

    getCells() {
        return this.cells;
    }

    getPage() {
        return this.page;
    }

    getContinuePayload() {
        return {
            jobId: this.jobId,
            page: this.page,
            cells: this.getCells(),
            template: this.getTemplate()
        };
    }
}
