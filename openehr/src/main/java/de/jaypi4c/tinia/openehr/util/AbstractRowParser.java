package de.jaypi4c.tinia.openehr.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public abstract class AbstractRowParser<T> implements RowParser<T> {

    Row parse(String[] row) {
        int i = 0;
        String activeIngredient = readValue(row, i++);
        String name = readValue(row, i++);
        String strength = readValue(row, i++);
        String form = readValue(row, i++);
        DosageSchema dosageSchema;
        if (row.length == 8) {
            dosageSchema = new CombinedDosageSchema(readValue(row, i++));
        } else {
            dosageSchema = new StandardDosageSchema(
                    readValue(row, i++),
                    readValue(row, i++),
                    readValue(row, i++),
                    readValue(row, i++)
            );
        }
        String unit = readValue(row, i++);
        String hint = readValue(row, i++);
        String reason = readValue(row, i++);

        return new Row(
                activeIngredient,
                name,
                strength,
                form,
                dosageSchema,
                unit,
                hint,
                reason
        );
    }

    private String readValue(String[] row, int index) {
        if (index >= row.length) {
            return "";
        }
        return row[index].trim();
    }

    Optional<StrengthQuantity> parseStaerke(String staerke) {
        if (staerke.isBlank())
            return Optional.empty();
        String[] staerkeParts = staerke.split(" ");
        if (staerkeParts.length == 2) {
            String einheit = staerkeParts[1];
            String clearString = staerkeParts[0].replaceAll("[^\\d.,]", "");
            clearString = clearString.replace(",", ".");
            log.debug("Parsing magnitude {} and unit {}", clearString, einheit);
            if (clearString.isBlank())
                return Optional.empty();
            double staerkeValue = Double.parseDouble(clearString.replace(",", "."));
            return Optional.of(new StrengthQuantity(staerkeValue, einheit));
        }
        return Optional.empty();
    }

    record StrengthQuantity(double magnitude, String unit) {
    }

}
