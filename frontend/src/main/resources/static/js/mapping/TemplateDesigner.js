import {MappingState} from "./MappingState.js";

import {MappingPanel} from "./MappingPanel.js";

import {MappingToolbox} from "./MappingToolbox.js";

/**
 * Coordinates mapping workflow.
 */
export class TemplateDesigner {

    constructor() {

        this.state =
            new MappingState();

        this.panel =
            new MappingPanel(
                "sidePanel"
            );

        this.toolbox =
            new MappingToolbox(
                this.panel,

                this.state,

                {

                    onTargetSelected:
                        field =>
                            this.selectBmpField(
                                field
                            ),

                    onCustomFieldCreated:
                        name =>
                            this.createCustomField(
                                name
                            ),

                    onCustomFieldRemoved:
                        id =>
                            this.removeCustomField(
                                id
                            )
                }
            );

        this.toolbox.refresh();
    }

    /**
     * Select BMP target.
     */
    selectBmpField(field) {

        this.state.selectTarget(
            field
        );

        this.toolbox.refresh();
    }

    /**
     * Create custom field.
     */
    createCustomField(name) {

        this.state.template
            .addCustomField(
                name,
                []
            );

        this.toolbox.refresh();
    }

    /**
     * Remove custom field.
     */
    removeCustomField(id) {

        this.state.template
            .removeCustomField(
                id
            );

        this.toolbox.refresh();
    }

    /**
     * Assigns a header cell.
     *
     * @param {Cell} cell
     */
    assignCell(cell) {

        if (
            !this.state.selectedTarget
        ) {

            return;
        }

        if (
            cell.type !== "HEADER"
        ) {

            alert(
                "Only header cells can be mapped."
            );

            return;
        }

        this.state.template
            .mapBmpField(
                this.state.selectedTarget,

                [cell.id]
            );

        cell.mapping =
            this.state.selectedTarget;

        this.toolbox.refresh();
    }

    /**
     * Returns current template.
     */
    getTemplate() {

        return this.state.template;
    }
}