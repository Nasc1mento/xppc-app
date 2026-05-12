package br.edu.ifpe.core.domain.enums;

public enum CourseRegime implements ILabeledEnum {
    FULL_TIME("Integral"),
    PART_TIME("Parcial");

    private final String label;

    CourseRegime(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
