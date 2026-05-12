package br.edu.ifpe.ui.swing.pages;

import br.edu.ifpe.ui.swing.components.Page;

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
