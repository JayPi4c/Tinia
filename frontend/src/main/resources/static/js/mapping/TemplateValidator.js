import {BmpFieldList} from "./BmpFields.js";

/**
 * Validates template completeness.
 */
export class TemplateValidator {

    /**
     * Validates template.
     *
     * @param {TemplateDefinition} template
     * @returns {Array}
     */
    static validate(template) {
        const result = [];

        BmpFieldList.forEach(field => {
                const mapping = template.bmpMappings.find(m =>
                    m.bmpField === field
                );
                result.push({
                    field,
                    valid: mapping && mapping.headerCellIds.length > 0
                });
            }
        );
        return result;
    }

    /**
     * Returns true if all BMP fields
     * are mapped.
     *
     * @param {TemplateDefinition} template
     * @returns {boolean}
     */
    static isComplete(template) {
        return this.validate(template)
            .every(entry => entry.valid);
    }
}
