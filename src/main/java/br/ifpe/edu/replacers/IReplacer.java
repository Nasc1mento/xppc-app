package br.ifpe.edu.replacers;

import br.ifpe.edu.replacers.helpers.DocumentHelper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public interface IReplacer extends Comparable<IReplacer> {

    void replace() throws IOException;
    int getPriority();

    default void commit(XWPFDocument d) throws IOException {
        try (var out = new FileOutputStream(DocumentHelper.getTempPath().toFile())) {
            d.write(out);
        }
    }

    @Override
    default int compareTo(IReplacer o) {
        return Integer.compare(getPriority(), o.getPriority());
    }
}
