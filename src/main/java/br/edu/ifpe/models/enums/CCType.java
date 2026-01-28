package br.edu.ifpe.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum CCType implements ILabeledEnum {
    MANDATORY("Obrigatória"),
    OPTIONAL("Optativa"),
    ELECTIVE("Eletiva");

    private final String label;
}
