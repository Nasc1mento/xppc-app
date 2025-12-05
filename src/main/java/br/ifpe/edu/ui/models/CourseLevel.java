package br.ifpe.edu.ui.models;

import java.util.Objects;

public enum CourseLevel {
    BACHELOR("Bacharelado"),
    TECHNOLOGIST("Superior de Tecnologia");

    private final String s;

    CourseLevel(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }

    public static CourseLevel findByString(String value) {
        for (var cc : CourseLevel.values()) {
            if (Objects.equals(cc.toString(), value)) {
                return cc;
            }
        }

        return null;
    }
}
