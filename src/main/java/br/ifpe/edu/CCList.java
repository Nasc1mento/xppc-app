package br.ifpe.edu;

import br.ifpe.edu.ui.models.CC;
import br.ifpe.edu.ui.models.CCType;

import java.util.ArrayList;
import java.util.List;

public enum CCList {

    INSTANCE;

    public static class Sum {
        public String totalHa = "0.00";
        public String totalHr =  "0.00";
        public String totalExt =  "0.00";
    }


    private final List<CC> list;

    CCList() {
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
