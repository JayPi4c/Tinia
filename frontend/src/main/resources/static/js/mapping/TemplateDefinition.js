/**
 * Complete template definition.
 */
export class TemplateDefinition {

    constructor() {

        this.version = 1;

        this.templateName = "";

        this.author = "";

        this.createdAt =
            new Date().toISOString();

        /**
         * BMP mappings.
         *
         * One BMP field can reference
         * multiple header cells.
         */
        this.bmpMappings = [];

        /**
         * Additional non-BMP columns.
         */
        this.customFields = [];

        /**
         * Editor notes.
         */
        this.notes = [];
    }

    /**
     * Maps a BMP field.
     *
     * @param {string} bmpField
     * @param {Array<string>} headerCellIds
     */
    mapBmpField(
        bmpField,
        headerCellIds
    ) {

        const existing =
            this.bmpMappings.find(
                m =>
                    m.bmpField === bmpField
            );

        if (existing) {

            existing.headerCellIds =
                headerCellIds;

            return;
        }

        this.bmpMappings.push({

            bmpField,

            headerCellIds
        });
    }

    /**
     * Adds a custom field.
     *
     * @param {string} name
     * @param {Array<string>} headerCellIds
     */
    addCustomField(
        name,
        headerCellIds
    ) {

        this.customFields.push({

            id:
                crypto.randomUUID(),

            name,

            headerCellIds
        });
    }

    /**
     * Removes BMP mapping.
     *
     * @param {string} bmpField
     */
    removeBmpMapping(
        bmpField
    ) {

        this.bmpMappings =
            this.bmpMappings.filter(
                m =>
                    m.bmpField !== bmpField
            );
    }

    /**
     * Removes custom field.
     *
     * @param {string} id
     */
    removeCustomField(id) {

        this.customFields =
            this.customFields.filter(
                f =>
                    f.id !== id
            );
    }

    /**
     * Finds mapping for a cell.
     *
     * @param {string} cellId
     * @returns {*|null}
     */
    getMappingForCell(cellId) {

        const bmp =
            this.bmpMappings.find(
                mapping =>
                    mapping.headerCellIds.includes(
                        cellId
                    )
            );

        if (bmp) {
            return bmp;
        }

        return this.customFields.find(
            field =>
                field.headerCellIds.includes(
                    cellId
                )
        ) || null;
    }

}