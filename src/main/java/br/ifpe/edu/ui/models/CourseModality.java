package br.ifpe.edu.ui.models;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum CourseModality {
    ON_SITE("Presencial"),
    HYBRID("Semipresencial"),
    ONLINE("Educação à distância");

    private final String s;

    @Override
    public String toString() {
        return s;
    }
}
