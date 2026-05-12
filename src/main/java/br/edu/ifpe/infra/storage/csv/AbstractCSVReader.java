package br.edu.ifpe.infra.storage.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.Builder;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;


public abstract class AbstractCSVReader {

    private static final Logger log = LoggerFactory.getLogger(AbstractCSVReader.class);

    private final String fileName;
    private final Charset charset;
    private final CSVFormat csvFormat;
    private List<CSVRecord> list;

    protected AbstractCSVReader(final String fileName, final Charset charset, final char separator) {
        this.fileName = fileName;
        this.charset = charset;

        csvFormat = Builder.create()
                .setDelimiter(separator)
                .setIgnoreEmptyLines(true)
                .setHeader()
                .setSkipHeaderRecord(true)
                .setTrim(true)
                .get();

        log.info("CSV reader initialized for {}", fileName);
    }

    protected List<CSVRecord> read() {
        if (list == null) {

            try (var reader = new InputStreamReader(
                    Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)),
                    charset
            )) {

            list = csvFormat.parse(reader).getRecords();

            log.info("File {} loaded successfully", fileName);

            } catch (IOException | NullPointerException e) {
                log.error("Failed processing CSV file {}", fileName, e);
                throw new RuntimeException("Failed to read csv file", e);
            }
        }

        return list;
    }

    protected String getAFromB(final int c1, final String b, final int c2) {
        return read().stream()
                .filter(line -> line.size() > Math.max(c1, c2))
                .filter(line -> Objects.equals(b, line.get(c1)))
                .map(line -> line.get(c2).trim())
                .findFirst()
                .orElse(null);
    }

    protected List<String> getAllFromA(final int a) {
        return read().stream().map(line -> line.get(a)).sorted().toList();
    }

    protected CSVRecord getFirstFromA(final int c, final String b) {
        return read().stream()
                .filter(line -> line.size() > c)
                .filter(line -> Objects.equals(b, line.get(c)))
                .findFirst()
                .orElse(null);
    }
}