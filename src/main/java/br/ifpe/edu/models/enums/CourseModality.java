package br.ifpe.edu.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum CourseModality implements ILabeledEnum<String> {
    ON_SITE("Presencial"),
    HYBRID("Semipresencial"),
    ONLINE("Educação à distância");

    private final String label;
}
