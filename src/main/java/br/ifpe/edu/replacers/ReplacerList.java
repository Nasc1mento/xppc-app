package br.ifpe.edu.replacers;

import br.ifpe.edu.replacers.helpers.CurrentTable;

import java.io.IOException;
import java.util.List;

public class ReplacerList implements AutoCloseable {

    private List<IReplacer> list;

    public ReplacerList() {
        loadList();
    }

    private void loadList() {
        list = List.of(
                new CurricularDrawReplacer(),
                new MatrixReplacer(),
                new OptionalComponentsReplacer(),
                new EletivosReplacer(),
                new EmentaryReplacer(),
                new HistoryReplacer(),
                new CurricularFormReplacer(),
                new PlaceholderReplacer()
        );
    }

    public void cAll() {
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
        CurrentTable.INSTANCE.setInitialValue();
    }
}
