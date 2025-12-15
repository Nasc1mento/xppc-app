package br.ifpe.edu.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;


@AllArgsConstructor
@Getter
public enum CCType implements ILabeledEnum<String> {
    MANDATORY("Obrigatória"),
    OPTIONAL("Optativa"),
    ELECTIVE("Eletiva");

    private final String label;

    public static CCType findByString(String value) {
        for (var cc : CCType.values()) {
            if (Objects.equals(cc.toString(), value)) {
                return cc;
            }
        }

        return null;
    }
}
