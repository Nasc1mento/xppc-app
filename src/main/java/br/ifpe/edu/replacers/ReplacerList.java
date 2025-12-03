package br.ifpe.edu.replacers;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ReplacerList {

    private final List<IReplacer> list;

    public ReplacerList(Path outputPath) {

        URL resource = Thread.currentThread().getContextClassLoader().getResource("ppc.docx");
        if (resource == null) {
            throw new RuntimeException("Template ppc.docx não encontrado no classpath");
        }

        Path templatePath = Paths.get(resource.getPath());

        this.list = List.of(
                new PlaceholderReplacer(templatePath, outputPath),
                new CurricularDrawReplacer(outputPath),
                new MatrixReplacer(outputPath),
                new OptionalComponentsReplacer(outputPath),
                new EletivosReplacer(outputPath),
                new EmentaryReplacer(outputPath),
                new HistoryReplacer(outputPath)
        );
    }

    public boolean callAll() {
        try {
            for (IReplacer replacer : list) {
                replacer.replace();
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
