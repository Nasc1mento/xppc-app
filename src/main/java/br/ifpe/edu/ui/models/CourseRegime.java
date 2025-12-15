package br.ifpe.edu.ui.models;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum CourseRegime {
    FULL_TIME("Integral"),
    PART_TIME("Parcial");

    private final String s;

    @Override
    public String toString() {
        return s;
    }
}
