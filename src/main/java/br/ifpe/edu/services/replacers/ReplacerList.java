package br.ifpe.edu.services.replacers;

import br.ifpe.edu.helpers.DocumentHelper;
import br.ifpe.edu.helpers.ParagraphHelper;
import br.ifpe.edu.helpers.TableLocationHelper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
public class ReplacerList implements AutoCloseable {

    private List<IReplacer> list;
    private final DocumentHelper documentHelper = DocumentHelper.INSTANCE;

    public ReplacerList() {
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

        documentHelper.createTempDoc();

        for (IReplacer replacer : list) {
            log.debug("Executing replacer {}", replacer.getClass().getSimpleName());

            replacer.replace();
        }

        documentHelper.save();

        long time = System.currentTimeMillis() - start;

        log.info("Document generation finished in {} ms", time);
    }


    @Override
    public void close() {
        log.debug("Resetting state");

        loadList();
        TableLocationHelper.INSTANCE.reset();
        ParagraphHelper.reset();
    }
}
