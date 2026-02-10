package br.edu.ifpe.enums;

public enum Situation implements ILabeledEnum {
    OPTION1("Apresentação Inicial do PPC"),
    OPTION2("Reformulação Integral do PPC"),
    OPTION3("Reformulação Parcial do PPC");

    private final String label;

    Situation(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
