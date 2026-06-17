import {TemplateDefinition} from "./TemplateDefinition.js";

/**
 * Import/export template JSON.
 */
export class TemplateSerializer {

    /**
     * Convert template
     * into JSON string.
     *
     * @param {TemplateDefinition} template
     * @returns {string}
     */
    static toJson(
        template
    ) {

        return JSON.stringify(
            template,
            null,
            2
        );
    }

    /**
     * Reads template JSON.
     *
     * @param {string} json
     * @returns {TemplateDefinition}
     */
    static fromJson(
        json
    ) {

        const parsed =
            JSON.parse(
                json
            );

        const template =
            new TemplateDefinition();

        Object.assign(
            template,
            parsed
        );

        return template;
    }

    /**
     * Downloads template.
     *
     * @param {TemplateDefinition} template
     */
    static download(
        template
    ) {

        const blob =
            new Blob(
                [
                    this.toJson(
                        template
                    )
                ],
                {
                    type:
                        "application/json"
                }
            );

        const url =
            URL.createObjectURL(
                blob
            );

        const link =
            document.createElement(
                "a"
            );

        link.href = url;

        link.download =
            "template.json";

        link.click();

        URL.revokeObjectURL(
            url
        );
    }
}