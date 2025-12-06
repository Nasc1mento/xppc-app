package br.ifpe.edu.replacers.helpers;

import java.io.InputStream;
import java.nio.file.Path;

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
}
