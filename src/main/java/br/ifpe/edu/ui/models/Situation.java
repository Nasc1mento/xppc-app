package br.ifpe.edu.ui.models;

public enum Situation {
    OPTION1("Apresentação Inicial do PPC"),
    OPTION2("Reformulação Integral do PPC"),
    OPTION3("Reformulação Parcial do PPC");

    private final String s;

    Situation(final String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}
