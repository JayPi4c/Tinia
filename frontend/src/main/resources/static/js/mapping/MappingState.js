import {TemplateDefinition} from "./TemplateDefinition.js";

/**
 * Runtime mapping state.
 */
export class MappingState {

    constructor() {

        this.active = false;

        /**
         * Currently selected BMP field.
         *
         * Example:
         * HANDELSNAME
         */
        this.selectedTarget =
            null;

        /**
         * Current template.
         */
        this.template =
            new TemplateDefinition();
    }

    /**
     * Clears current target.
     */
    clearSelection() {

        this.selectedTarget =
            null;
    }

    /**
     * Select target BMP field.
     *
     * @param {string} field
     */
    selectTarget(field) {

        this.selectedTarget =
            field;
    }
}