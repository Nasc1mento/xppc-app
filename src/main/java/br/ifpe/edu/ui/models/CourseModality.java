package br.ifpe.edu.ui.models;

public enum CourseModality {
    ON_SITE("Presencial"),
    HYBRID("Semipresencial"),
    ONLINE("Educação à distância");

    private final String s;

    CourseModality(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}
