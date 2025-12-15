package br.ifpe.edu.ui.models;

import lombok.AllArgsConstructor;

import java.util.Objects;


@AllArgsConstructor
public enum CCType {
    MANDATORY("Obrigatória"),
    OPTIONAL("Optativa"),
    ELECTIVE("Eletiva");

    private final String s;

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
