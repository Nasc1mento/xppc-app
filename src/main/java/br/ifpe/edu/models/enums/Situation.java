package br.ifpe.edu.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum Situation implements ILabeledEnum {
    OPTION1("Apresentação Inicial do PPC"),
    OPTION2("Reformulação Integral do PPC"),
    OPTION3("Reformulação Parcial do PPC");

    private final String label;
}
