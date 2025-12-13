package br.ifpe.edu.replacers;

import br.ifpe.edu.replacers.helpers.DocumentHelper;
import br.ifpe.edu.replacers.helpers.ParagraphHelper;
import br.ifpe.edu.replacers.helpers.TableLocationHelper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReplacerList implements AutoCloseable {

    private List<IReplacer> list;
    private final DocumentHelper documentHelper = DocumentHelper.INSTANCE;

    public ReplacerList() {
        loadList();
    }

    private void loadList() {
                this.list = Stream.of(
                new CurricularDrawReplacer(),
                new CurricularFormReplacer(),
                new EletivosReplacer(),
                new EmentaryReplacer(),
                new HistoryReplacer(),
                new MatrixReplacer(),
                new OptionalComponentsReplacer(),
                new PlaceholderReplacer()
        ).sorted().collect(Collectors.toList());
    }

    public void cAll() throws IOException {
        documentHelper.createTempDoc();

        for (IReplacer replacer : list) {
            replacer.replace();
        }

        documentHelper.save();
    }


    @Override
    public void close() {
        loadList();
        TableLocationHelper.INSTANCE.reset();
        ParagraphHelper.reset();
    }
}
