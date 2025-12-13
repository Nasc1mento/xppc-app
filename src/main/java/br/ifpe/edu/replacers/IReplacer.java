package br.ifpe.edu.replacers;

import br.ifpe.edu.replacers.helpers.DocumentHelper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileOutputStream;
import java.io.IOException;

public interface IReplacer {
    void replace() throws IOException;

    default void commit(XWPFDocument d) throws IOException {
        try (var out = new FileOutputStream(DocumentHelper.getTempPath().toFile())) {
            d.write(out);
        }
    }
}
