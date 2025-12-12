package br.ifpe.edu.replacers;

import br.ifpe.edu.replacers.helpers.DocumentPath;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public interface IReplacer {
    void replace() throws IOException;

    default void save(XWPFDocument d) throws IOException {
        try (var out = new FileOutputStream(DocumentPath.getTempPath().toFile())) {
            d.write(out);
        }
    }

    default void save() throws IOException {
        Files.move(
                DocumentPath.getTempPath(),
                DocumentPath.INSTANCE.getOutputPath(),
                StandardCopyOption.REPLACE_EXISTING
        );
    }
}
