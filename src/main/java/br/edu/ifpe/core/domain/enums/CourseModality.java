package br.edu.ifpe.core.domain.enums;

public enum CourseModality implements ILabeledEnum {
    ON_SITE("Presencial"),
    HYBRID("Semipresencial"),
    ONLINE("Educação à distância");

    private final String label;

    CourseModality(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
