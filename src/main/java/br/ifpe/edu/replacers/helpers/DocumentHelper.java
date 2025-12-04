package br.ifpe.edu.replacers.helpers;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum DocumentHelper {

    INSTANCE;

    private Path outputPath;

    public Path getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(final Path outputPath) {
        this.outputPath = outputPath;
    }

    public static Path loadTemplatePath() {
        try {
            var url = Thread.currentThread().getContextClassLoader().getResource("ppc.docx");
            if (url == null) throw new IllegalArgumentException("Template not found: " + "ppc.docx");
            return Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
