package br.ifpe.edu.replacers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ReplacerList {

    private static final Path teplatePath;

    static {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("ppc.docx");
        if (resource == null) {
            throw new RuntimeException(new FileNotFoundException("Template ppc.docx não encontrado no classpath"));
        }

        teplatePath = Paths.get(resource.getPath());
    }

    private static final List<IReplacer> list = List.of(
            new PlaceholderReplacer(teplatePath, Paths.get(System.getProperty("user.dir"), "ppc.docx")),
            new MatrixReplacer(Paths.get(System.getProperty("user.dir"), "ppc.docx")),
            new HistoryReplacer(Paths.get(System.getProperty("user.dir"), "ppc.docx"))
    );

    public static boolean callAll() {
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
