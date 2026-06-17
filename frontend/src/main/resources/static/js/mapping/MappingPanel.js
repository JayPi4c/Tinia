import {BmpFieldList} from "./BmpFields.js";

/**
 * Right side mapping panel.
 */
export class MappingPanel {

    constructor(containerId) {

        this.container =
            document.getElementById(
                containerId
            );
    }

    /**
     * Renders panel.
     *
     * @param {MappingState} state
     */
    render(state) {

        this.container.innerHTML = "";

        const wrapper =
            document.createElement(
                "div"
            );

        wrapper.className =
            "card";

        wrapper.innerHTML = `
            <div class="card-body">

                <h5>
                    Template Designer
                </h5>

                <div id="bmpFieldList"></div>

                <hr>

                <h6>
                    Custom Fields
                </h6>

                <div id="customFieldList"></div>

                <button
                    id="addCustomFieldBtn"
                    class="btn btn-sm btn-success mt-2">
                    Add Custom Field
                </button>

                <hr>

                <h6>
                    Template
                </h6>

                <input
                    id="templateName"
                    class="form-control mb-2"
                    placeholder="Template Name">

                <input
                    id="templateAuthor"
                    class="form-control mb-2"
                    placeholder="Author">

                <button
                    id="exportTemplateBtn"
                    class="btn btn-primary w-100">
                    Export Template
                </button>

            </div>
        `;

        this.container.appendChild(
            wrapper
        );

        this.renderBmpFields(
            state
        );

        this.renderCustomFields(
            state
        );
    }

    renderBmpFields(state) {

        const container =
            document.getElementById(
                "bmpFieldList"
            );

        BmpFieldList.forEach(
            field => {

                const button =
                    document.createElement(
                        "button"
                    );

                button.className =
                    "btn btn-outline-primary btn-sm w-100 mb-1";

                button.dataset.field =
                    field;

                button.textContent =
                    field;

                if (
                    state.selectedTarget === field
                ) {

                    button.classList.add(
                        "active"
                    );
                }

                container.appendChild(
                    button
                );
            }
        );
    }

    renderCustomFields(state) {

        const container =
            document.getElementById(
                "customFieldList"
            );

        state.template.customFields
            .forEach(field => {

                const row =
                    document.createElement(
                        "div"
                    );

                row.className =
                    "d-flex justify-content-between align-items-center mb-1";

                row.innerHTML = `
                    <span>
                        ${field.name}
                    </span>

                    <button
                        class="btn btn-sm btn-danger"
                        data-custom-id="${field.id}">
                        x
                    </button>
                `;

                container.appendChild(
                    row
                );
            });
    }
}