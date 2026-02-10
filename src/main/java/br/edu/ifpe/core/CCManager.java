package br.edu.ifpe.core;

import br.edu.ifpe.enums.CCType;
import br.edu.ifpe.models.CC;
import br.edu.ifpe.utils.Eval;

import java.util.ArrayList;
import java.util.List;

public enum CCManager {

    INSTANCE;

    public static class Sum {
        private String totalHa = "0.00";
        private String totalHr =  "0.00";
        private String totalExt =  "0.00";

        public String getTotalExt() {
            return totalExt;
        }

        public String getTotalHa() {
            return totalHa;
        }

        public String getTotalHr() {
            return totalHr;
        }
    }


    private final List<CC> list;

    CCManager() {
        this.list = new ArrayList<>();
    }

    public void add(final CC cc) {
        list.add(cc);
    }

    public void remove(final String name) {
        list.removeIf(cc -> cc.name().equals(name));
    }

    public List<CC> getList() {
        return List.copyOf(this.list);
    }

    public Sum getSum(final List<CC> list, final CCType ccType) {
        var total = new Sum();
        for (CC cc : list) {
            if (cc.type().equals(ccType)) {
                total.totalHa = Eval.evalDecimal("%s+%s", total.totalHa, cc.ha());
                total.totalHr = Eval.evalDecimal("%s+%s", total.totalHr, cc.hr());
                total.totalExt = Eval.evalDecimal("%s+%s", total.totalExt, cc.ext());
            }
        }

        return total;
    }
}
