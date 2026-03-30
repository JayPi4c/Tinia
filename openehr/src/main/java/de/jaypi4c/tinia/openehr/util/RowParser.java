package de.jaypi4c.tinia.openehr.util;

public interface RowParser<T> {

    T parseRow(String[] row);

    interface DosageSchema {
        boolean isCombinedDosageSchema();
    }

    record StandardDosageSchema(
            String morning,
            String noon,
            String evening,
            String night
    ) implements DosageSchema {
        @Override
        public boolean isCombinedDosageSchema() {
            return false;
        }
    }

    record CombinedDosageSchema(
            String combinedDosage
    ) implements DosageSchema {
        @Override
        public boolean isCombinedDosageSchema() {
            return true;
        }
    }

    record Row(String activeIngredient, String name, String strength, String form, DosageSchema dosageSchema,
               String unit, String hint, String reason) {

    }

}
