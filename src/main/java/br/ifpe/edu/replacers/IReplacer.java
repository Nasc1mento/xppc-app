package br.ifpe.edu.replacers;

import br.ifpe.edu.replacers.helpers.DocumentHelper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public interface IReplacer {
    void replace() throws IOException;

    default void save(XWPFDocument d) {
        try (var out = new FileOutputStream(DocumentHelper.getTempPath().toFile())) {
            d.write(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default void save() {
        try {
            Files.move(
                    DocumentHelper.getTempPath(),
                    DocumentHelper.INSTANCE.getOutputPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
