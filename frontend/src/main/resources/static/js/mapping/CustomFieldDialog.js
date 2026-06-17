/**
 * Handles creation
 * of custom fields.
 */
export class CustomFieldDialog {

    /**
     * Opens dialog.
     *
     * @returns {string|null}
     */
    static open() {

        const result =
            prompt(
                "Custom field name:"
            );

        if (
            !result ||
            !result.trim()
        ) {
            return null;
        }

        return result.trim();
    }
}