/**
 * Handles upload related backend communication.
 */
export class UploadApi {

    /**
     * @param {string} backendUrl
     */
    constructor(backendUrl) {
        this.backendUrl = backendUrl;
    }

    /**
     * Uploads a file to the backend.
     *
     * Expected response:
     *
     * {
     *   jobId: "..."
     * }
     *
     * @param {File} file
     * @param {boolean} processOcr
     * @returns {Promise<Object>}
     */
    async upload(file, processOcr) {
        const formData = new FormData();
        formData.append("file", file);
        formData.append("processOcr", processOcr);

        const response = await fetch(
            `${this.backendUrl}/api/v1/upload`,
            {
                method: "POST",
                body: formData
            }
        );

        if (response.status !== 202) {
            const text = await response.text();
            throw new Error(text);
        }
        return response.json();
    }

    async continueProcessing(jobId, page, cells, template) {
        const response = await fetch(`${this.backendUrl}/api/v1/upload/jobs/${jobId}/pages/${page}/continue`,
            {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({
                    cells, template
                })
            }
        );

        if (!response.ok) {
            throw new Error(await response.text());
        }
    }

}
