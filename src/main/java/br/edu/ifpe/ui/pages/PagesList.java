package br.edu.ifpe.ui.pages;

import br.edu.ifpe.ui.components.Page;

import java.util.List;

public class PagesList {

    private static final List<Page> PAGES = List.of(
            new Cover(),
            new Proponent(),
            new Course(),
            new CourseSituation(),
            new CurricularComponents(),
            new Generation()
    );

    public static List<Page> getList() {
        return PAGES;
    }
}
