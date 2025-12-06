package br.ifpe.edu.ui;

import br.ifpe.edu.ui.common.Page;
import br.ifpe.edu.ui.pages.*;

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
