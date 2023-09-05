package com.jaypi4c;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public record Medication(String Wirkstoff, String Handelsname, String Staerke, String Form, float morgens,
                         float mittags, float abends, float zurNacht, String Einheit, String Hinweise, String Grund) {

    /**
     * Converts a 2D String array to an array of Medication objects
     *
     * @param medicationPlan 2D String array, the first row is the header of the medicationplan.
     *                       The whole array is expected to not have any null values and the number of elements in the array match the entries in the medicationplan.
     * @return Array of Medication objects
     */
    public static Medication[] fromStringArray(String[][] medicationPlan) {
        Medication[] medications = new Medication[medicationPlan.length - 1];
        for (int i = 1; i < medicationPlan.length; i++) {
            try {
                String wirkstoff = medicationPlan[i][0];
                String handelsname = medicationPlan[i][1];
                String staerke = medicationPlan[i][2];
                String form = medicationPlan[i][3];
                float morgens = Float.parseFloat(medicationPlan[i][4]);
                float mittags = Float.parseFloat(medicationPlan[i][5]);
                float abends = Float.parseFloat(medicationPlan[i][6]);
                float zurNacht = Float.parseFloat(medicationPlan[i][7]);
                String einheit = medicationPlan[i][8];
                String hinweise = medicationPlan[i][9];
                String grund = medicationPlan[i][10];
                medications[i - 1] = new Medication(wirkstoff, handelsname, staerke, form, morgens, mittags, abends, zurNacht, einheit, hinweise, grund);

            } catch (Exception e) {
                log.error("Failed to parse medication row {}. Error: {}", i, e);
            }
        }
        return medications;
    }

}
