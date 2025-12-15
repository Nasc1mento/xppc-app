package br.ifpe.edu.models;

import br.ifpe.edu.utils.Eval;
import br.ifpe.edu.models.enums.CCType;

public record CC(
    String code,
    String name,
    CCType type,
    String period,
    String ap,
    String at,
    String ae,
    String hrPr,
    String hrTeo,
    String ext,
    String prereq,
    String coreq
) {

    public String credits() {
        return Eval.evalInteger("%s+%s+%s", ap, at, ae);
    }

    public String hr() {
        return Eval.evalInteger("%s+%s", hrPr, hrTeo);
    }

    public String ha() {
        return Eval.evalInteger("((%s + %s) *60)/45", hr(), ext());
    }

}
