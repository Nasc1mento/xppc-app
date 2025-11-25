package br.ifpe.edu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum CurricularComponentList {

    INSTANCE;

    public record CC (
        String code,
        String name,
        String type,
        String period,
        String credits,
        String ha,
        String hr,
        String ext,
        String prereq,
        String coreq
    ) { }


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

    public List<CC> filterByType(final String type) {
        return list.stream()
                .filter(cc -> cc.type().equals(type))
                .collect(Collectors.toList());
    }
}
