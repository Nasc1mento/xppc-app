package br.ifpe.edu;

import br.ifpe.edu.ui.models.CC;
import br.ifpe.edu.ui.models.CCType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum CurricularComponentList {

    INSTANCE;

    public static class Sum {
        public String totalHa = "0.00";
        public String totalHr =  "0.00";
        public String totalExt =  "0.00";
    }


    private final List<CC> list;

    CurricularComponentList() {
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

    public Sum getSum(List<CC> list, CCType ccType) {
        var total = new Sum();
        for (CC cc : list) {
            if (cc.type().equals(ccType)) {
                total.totalHa = Eval.eval("%s+%s", total.totalHa, cc.ha());
                total.totalHr = Eval.eval("%s+%s", total.totalHr, cc.hr());
                total.totalExt = Eval.eval("%s+%s", total.totalExt, cc.ext());
            }
        }

        return total;
    }
}
