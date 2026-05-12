package br.edu.ifpe.core;

import br.edu.ifpe.core.replacers.*;
import br.edu.ifpe.infra.doc.DocumentCursor;
import br.edu.ifpe.infra.doc.DocumentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Generator implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(Generator.class);


    private List<IReplacer> list;
    private final DocumentManager documentManager = DocumentManager.INSTANCE;

    public Generator() {
        loadList();
    }

    private void loadList() {
        this.list = Stream.of(
            new CurricularDrawReplacer(),
            new CurricularFormReplacer(),
            new EletivosReplacer(),
            new EmentaryReplacer(),
            new HistoryReplacer(),
            new MatrixReplacer(),
            new OptionalComponentsReplacer(),
            new PlaceholderReplacer()
        ).sorted().collect(Collectors.toList());

        log.debug("Loaded {} replacers", this.list.size());
    }

    public void cAll() throws IOException {

        log.info("Document generation started");

        long start = System.currentTimeMillis();

        documentManager.createTempDoc();

        for (IReplacer replacer : list) {
            log.debug("Executing replacer {}", replacer.getClass().getSimpleName());

            replacer.replace();
        }

        documentManager.save();

        long time = System.currentTimeMillis() - start;

        log.info("Document generation finished in {} ms", time);
    }


    @Override
    public void close() {
        log.debug("Resetting state");

        loadList();
        DocumentCursor.INSTANCE.reset();
    }
}
