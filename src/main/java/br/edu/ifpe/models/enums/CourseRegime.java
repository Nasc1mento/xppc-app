package br.edu.ifpe.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum CourseRegime implements ILabeledEnum {
    FULL_TIME("Integral"),
    PART_TIME("Parcial");

    private final String label;
}
