package br.edu.ifpe.core.domain.enums;

public enum CCType implements ILabeledEnum {
    MANDATORY("Obrigatória"),
    OPTIONAL("Optativa"),
    ELECTIVE("Eletiva");

    private final String label;

    CCType(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
