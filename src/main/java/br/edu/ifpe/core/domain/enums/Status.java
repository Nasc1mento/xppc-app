package br.edu.ifpe.core.domain.enums;

public enum Status implements ILabeledEnum {
    OPTION1("Aguardando autorização do conselho superior"),
    OPTION2("Autorizado pelo conselho superior"),
    OPTION3("Aguardando reconhecimento do MEC"),
    OPTION4("Reconhecido pelo MEC"),
    OPTION5("Cadastrado no SISTEC");

    private final String label;

    Status(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
