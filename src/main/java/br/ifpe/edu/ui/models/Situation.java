package br.ifpe.edu.ui.models;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum Situation {
    OPTION1("Apresentação Inicial do PPC"),
    OPTION2("Reformulação Integral do PPC"),
    OPTION3("Reformulação Parcial do PPC");

    private final String s;

    @Override
    public String toString() {
        return s;
    }
}
