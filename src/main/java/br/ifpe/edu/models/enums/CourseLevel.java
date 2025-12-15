package br.ifpe.edu.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;


@AllArgsConstructor
@Getter
public enum CourseLevel implements ILabeledEnum<String> {
    BACHELOR("Bacharelado"),
    TECHNOLOGIST("Superior de Tecnologia");

    private final String label;

    public static CourseLevel findByString(String value) {
        for (var cc : CourseLevel.values()) {
            if (Objects.equals(cc.toString(), value)) {
                return cc;
            }
        }

        return null;
    }
}
