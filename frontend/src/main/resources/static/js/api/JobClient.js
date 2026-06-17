/**
 * Handles SSE communication
 * with processing jobs.
 */
export class JobClient {

    /**
     * @param {string} backendUrl
     */
    constructor(backendUrl) {
        this.backendUrl = backendUrl;
        this.eventSource = null;
    }

    /**
     * Starts listening to a job.
     *
     * @param {string} jobId
     * @param {Object} callbacks
     */
    connect(jobId, callbacks) {
        this.disconnect();

        this.eventSource = new EventSource(`${this.backendUrl}/api/v1/upload/jobs/${jobId}/updates`);

        this.eventSource.onmessage = event => {
            const data = JSON.parse(event.data);
            callbacks.onResult?.(data);
        };

        this.eventSource.onerror = error => {
            callbacks.onError?.(error);
            this.disconnect();
        };
    }

    /**
     * Closes current connection.
     */
    disconnect() {
        if (this.eventSource) {
            this.eventSource.close();
            this.eventSource = null;
        }
    }

    /**
     * Returns true if connected.
     *
     * @returns {boolean}
     */
    isConnected() {
        return (this.eventSource !== null);
    }
}
