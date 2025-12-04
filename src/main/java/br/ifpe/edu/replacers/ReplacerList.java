package br.ifpe.edu.replacers;

import java.io.IOException;
import java.util.List;

public class ReplacerList implements AutoCloseable {

    private List<IReplacer> list;

    public ReplacerList() {
        loadList();
    }

    private void loadList() {
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

    public void callAll() {

        try {
            for (IReplacer replacer : list) {
                replacer.replace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void close() {
        loadList();
    }
}
