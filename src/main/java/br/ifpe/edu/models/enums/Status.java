package br.ifpe.edu.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum Status implements ILabeledEnum<String> {
    OPTION1("Aguardando autorização do conselho superior"),
    OPTION2("Autorizado pelo conselho superior"),
    OPTION3("Aguardando reconhecimento do MEC"),
    OPTION4("Reconhecido pelo MEC"),
    OPTION5("Cadastrado no SISTEC");

    private final String label;
}
