package br.edu.ifpe.core.domain.enums;

public enum CourseLevel implements ILabeledEnum {
    BACHELOR("Bacharelado"),
    TECHNOLOGIST("Superior de Tecnologia");

    private final String label;

    CourseLevel(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
