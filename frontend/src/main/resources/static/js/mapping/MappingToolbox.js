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
        this.registerTemplateMetadataEvents();
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

    registerTemplateMetadataEvents() {
        document.getElementById("templateName")
            ?.addEventListener("input", event => {
                this.state.template.templateName = event.target.value;
            });

        document.getElementById("templateAuthor")
            ?.addEventListener("input", event => {
                this.state.template.author = event.target.value;
            });
    }

    syncTemplateMetadata() {
        const templateName = document.getElementById("templateName")?.value ?? "";
        const templateAuthor = document.getElementById("templateAuthor")?.value ?? "";

        this.state.template.templateName = templateName;
        this.state.template.author = templateAuthor;
    }

    registerExportEvent() {
        document.getElementById("exportTemplateBtn")
            ?.addEventListener("click", () => {
                    this.syncTemplateMetadata();

                    TemplateSerializer.download(this.state.template);
                }
            );
    }

    registerContinueEvent() {
        document.getElementById("continueBtn")
            ?.addEventListener("click", async () => {
                try {
                    this.syncTemplateMetadata();

                    await this.callbacks.onContinue?.();
                } catch (error) {
                    alert(error.message);
                }
            });
    }

}
