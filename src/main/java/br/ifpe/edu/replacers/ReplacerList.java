package br.ifpe.edu.replacers;

import br.ifpe.edu.replacers.helpers.ParagraphHelper;
import br.ifpe.edu.replacers.helpers.TableLocationHelper;
import br.ifpe.edu.replacers.helpers.DocumentHelper;

import java.io.IOException;
import java.util.List;

public class ReplacerList implements AutoCloseable {

    private List<IReplacer> list;
    private final DocumentHelper documentHelper = DocumentHelper.INSTANCE;

    public ReplacerList() {
        loadList();
    }

    private void loadList() {
        list = List.of(
                new CurricularDrawReplacer(),       //1
                new MatrixReplacer(),               //2
                new OptionalComponentsReplacer(),   //3
                new EletivosReplacer(),             //4
                new EmentaryReplacer(),             //5
                new HistoryReplacer(),              //6
                new CurricularFormReplacer(),       //7
                new PlaceholderReplacer()
        );
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
