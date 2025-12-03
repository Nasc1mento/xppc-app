package br.ifpe.edu.ui.models;

public enum CourseRegime {
    FULL_TIME("Integral"),
    PART_TIME("Parcial");

    private final String s;

    CourseRegime(final String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}
