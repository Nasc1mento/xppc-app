package br.ifpe.edu.ui.models;

import java.util.Objects;

public enum CCType {
    MANDATORY("Obrigatória"),
    OPTIONAL("Optativa"),
    ELECTIVE("Eletiva");

    private final String s;

    CCType(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }

    public static CCType findByString(String value) {
        for (var cc : CCType.values()) {
            if (Objects.equals(cc.toString(), value)) {
                return cc;
            }
        }

        return null;
    }
}
