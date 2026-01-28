package br.edu.ifpe.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum CourseLevel implements ILabeledEnum {
    BACHELOR("Bacharelado"),
    TECHNOLOGIST("Superior de Tecnologia");

    private final String label;
}
