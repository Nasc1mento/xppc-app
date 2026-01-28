package br.edu.ifpe.services.replacers;

import br.edu.ifpe.services.DocumentManager;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileOutputStream;
import java.io.IOException;

public interface IReplacer extends Comparable<IReplacer> {

    void replace() throws IOException;
    int getPriority();

    default void commit(XWPFDocument d) throws IOException {
        try (var out = new FileOutputStream(DocumentManager.INSTANCE.getTempPath().toFile())) {
            d.write(out);
        }
    }

    @Override
    default int compareTo(IReplacer r) {
        return Integer.compare(getPriority(), r.getPriority());
    }
}
