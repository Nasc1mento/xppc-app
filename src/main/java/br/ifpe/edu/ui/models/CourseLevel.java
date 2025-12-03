package br.ifpe.edu.ui.models;

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
}
