package br.ifpe.edu.replacers.helpers;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public enum DocumentPath {

    INSTANCE;

    private Path outputPath;

    public Path getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(final Path outputPath) {
        this.outputPath = outputPath;
    }

    public static InputStream loadResourceStream(final String name) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);

        if (is == null) {
            throw new IllegalArgumentException("Template not found: ppc.docx");
        }

        return is;
    }

    public static Path getTempPath() {
        return Path.of("ppc_temp.docx");
    }

    public void createTempDoc() throws IOException {
        try (XWPFDocument doc = new XWPFDocument(loadResourceStream("ppc.docx"))) {
            try (var out = new FileOutputStream(getTempPath().toFile())) {
                doc.write(out);
            }
        }
    }

    public void save() throws IOException {
        Files.move(
                getTempPath(),
                getOutputPath(),
                StandardCopyOption.REPLACE_EXISTING
        );
    }
}
