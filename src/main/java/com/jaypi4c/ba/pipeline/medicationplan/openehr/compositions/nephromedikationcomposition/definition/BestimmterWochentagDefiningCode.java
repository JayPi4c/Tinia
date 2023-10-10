package com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition.definition;

import org.ehrbase.openehr.sdk.generator.commons.interfaces.EnumValueSet;

public enum BestimmterWochentagDefiningCode implements EnumValueSet {
    MONTAG("Montag", "Die Aktivität sollte am Montag stattfinden.", "local", "at0007"),

    SONNTAG("Sonntag", "Die Aktivität sollte am Sonntag stattfinden.", "local", "at0020"),

    DIENSTAG("Dienstag", "Die Aktivität sollte am Dienstag stattfinden.", "local", "at0008"),

    MITTWOCH("Mittwoch", "Die Aktitvität sollte am Mittwoch stattfinden.", "local", "at0016"),

    SAMSTAG("Samstag", "Die Aktivität sollte am Samstag stattfinden.", "local", "at0019"),

    DONNERSTAG("Donnerstag", "Die Aktivität sollte am Donnerstag stattfinden.", "local", "at0017"),

    FREITAG("Freitag", "Die Aktivität sollte am Freitag stattfinden.", "local", "at0018");

    private String value;

    private String description;

    private String terminologyId;

    private String code;

    BestimmterWochentagDefiningCode(String value, String description, String terminologyId,
                                    String code) {
        this.value = value;
        this.description = description;
        this.terminologyId = terminologyId;
        this.code = code;
    }

    public String getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public String getTerminologyId() {
        return this.terminologyId;
    }

    public String getCode() {
        return this.code;
    }
}
