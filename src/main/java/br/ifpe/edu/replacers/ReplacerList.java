package br.ifpe.edu.replacers;

import br.ifpe.edu.replacers.helpers.CurrentTable;
import br.ifpe.edu.replacers.helpers.DocumentPath;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReplacerList implements AutoCloseable {

    private List<IReplacer> list;
    private final DocumentPath  documentPath = DocumentPath.INSTANCE;

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
        documentPath.createTempDoc();

        for (IReplacer replacer : list) {
            replacer.replace();
        }

        documentPath.save();
    }


    @Override
    public void close() {
        loadList();
        CurrentTable.INSTANCE.setInitialValue();
    }
}
