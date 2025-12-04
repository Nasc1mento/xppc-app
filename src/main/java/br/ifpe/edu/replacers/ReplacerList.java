package br.ifpe.edu.replacers;

import java.io.IOException;
import java.util.List;

public class ReplacerList {

    private final List<IReplacer> list;

    public ReplacerList() {


        this.list = List.of(
                new PlaceholderReplacer(),
                new CurricularDrawReplacer(),
                new MatrixReplacer(),
                new OptionalComponentsReplacer(),
                new EletivosReplacer(),
                new EmentaryReplacer(),
                new HistoryReplacer()
        );
    }

    public boolean callAll() {
        try {
            for (IReplacer replacer : list) {
                replacer.replace();
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
