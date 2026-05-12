package br.edu.ifpe.infra.doc;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


public enum DocumentManager {

    INSTANCE;

    private static final Logger log = LoggerFactory.getLogger(DocumentManager.class);

    private Path outputPath;
    private final Path tempPath = Path.of("ppc_temp.docx");

    public void setOutputPath(final Path outputPath) {
        this.outputPath = outputPath;

        log.debug("Output path defined: {}", this.outputPath);
    }

    public Path getOutputPath() {
        return outputPath;
    }

    public Path getTempPath() {
        return tempPath;
    }

    public static InputStream loadResourceStream(final String name) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);

        if (is == null) {
            log.error("Resource not found: {}", name);

            throw new IllegalArgumentException("Template not found: ppc.docx");
        }

        return is;
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
