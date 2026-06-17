/**
 * Displays bootstrap alerts.
 */
export class AlertService {

    /**
     * @param {string} containerId
     */
    constructor(containerId = "alertPlaceholder") {
        this.container = document.getElementById(containerId);
    }

    /**
     * Displays an alert.
     *
     * @param {string} message
     * @param {string} type
     */
    show(message, type = "info") {
        this.container.innerHTML = `
            <div class="alert alert-${type}" role="alert">
                ${message}
            </div>
        `;
    }

    /**
     * Success alert.
     *
     * @param {string} message
     */
    success(message) {
        this.show(message, "success");
    }

    /**
     * Error alert.
     *
     * @param {string} message
     */
    error(message) {
        this.show(message, "danger");
    }

    /**
     * Warning alert.
     *
     * @param {string} message
     */
    warning(message) {
        this.show(message, "warning");
    }

    /**
     * Clears alerts.
     */
    clear() {
        this.container.innerHTML = "";
    }
}
