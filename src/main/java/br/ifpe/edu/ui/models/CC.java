package br.ifpe.edu.ui.models;

public record CC(
    String code,
    String name,
    CCType type,
    String period,
    String credits,
    String ha,
    String hr,
    String ext,
    String prereq,
    String coreq
) { }
