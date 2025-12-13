package br.ifpe.edu.ui.models;

import br.ifpe.edu.Eval;

public record CC(
    String code,
    String name,
    CCType type,
    String period,
    String ap,
    String at,
    String hrPr,
    String hrTeo,
    String ext,
    String prereq,
    String coreq
) {

    public String credits() {
        return Eval.evalInteger("%s+%s", ap, at);
    }

    public String hr() {
        return Eval.evalDecimal("%s+%s", hrPr, hrTeo);
    }

    public String ha() {
        return Eval.evalDecimal("((%s + %s) *60)/45", hr(), ext());
    }

}
