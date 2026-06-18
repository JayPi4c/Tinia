import {TemplateSerializer} from "./TemplateSerializer.js";
import {CustomFieldDialog} from "./CustomFieldDialog.js";

/**
 * Handles mapping panel events.
 */
export class MappingToolbox {

    constructor(panel, state, callbacks) {
        this.panel = panel;
        this.state = state;
        this.callbacks = callbacks;
    }

    initialize() {
        this.registerBmpFieldEvents();
        this.registerCustomFieldEvents();
        this.registerExportEvent();
        this.registerContinueEvent();
    }

    refresh() {
        this.panel.render(this.state);
        this.initialize();
    }

    registerBmpFieldEvents() {
        document.querySelectorAll("#bmpFieldList button")
            .forEach(button => {
                button.addEventListener("click", () => {
                        const field = button.dataset.field;
                        this.callbacks.onTargetSelected?.(field);
                    }
                );
            });
    }


    registerCustomFieldEvents() {
        document.getElementById("addCustomFieldBtn")
            ?.addEventListener("click", () => {
                    const name = CustomFieldDialog.open();
                    if (!name) {
                        return;
                    }

                    this.callbacks.onCustomFieldCreated?.(name);
                }
            );

        document.querySelectorAll("[data-custom-id]")
            .forEach(button => {
                button.addEventListener("click", () => {
                        this.callbacks.onCustomFieldRemoved?.(button.dataset.customId);
                    }
                );
            });
    }

    registerExportEvent() {
        document.getElementById("exportTemplateBtn")
            ?.addEventListener("click", () => {
                    this.state.template.templateName = document.getElementById("templateName").value;
                    this.state.template.author = document.getElementById("templateAuthor").value;

                    TemplateSerializer.download(this.state.template);
                }
            );
    }

    registerContinueEvent() {
        document.getElementById("continueBtn")
            ?.addEventListener("click", () => {
                console.log("User wants to continue with the pipeline.");
                this.callbacks.onContinue?.();
            });
    }

}
