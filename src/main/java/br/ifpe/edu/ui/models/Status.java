package br.ifpe.edu.ui.models;

public enum Status {
    OPTION1("Aguardando autorização do conselho superior"),
    OPTION2("Autorizado pelo conselho superior"),
    OPTION3("Aguardando reconhecimento do MEC"),
    OPTION4("Reconhecido pelo MEC"),
    OPTION5("Cadastrado no SISTEC");

    private final String s;

    Status(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}
