package br.ifpe.edu.readers;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.Builder;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;


@Slf4j
public abstract class AbstractCSVReader {

    private final String fileName;
    private final Charset charset;
    private final CSVFormat csvFormat;
    private List<CSVRecord> list;

    protected AbstractCSVReader(String fileName, Charset charset, char separator) {
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

    protected String getAFromB(int c1, String b, int c2) {
        return read().stream()
                .filter(line -> line.size() > Math.max(c1, c2))
                .filter(line -> Objects.equals(b, line.get(c1)))
                .map(line -> line.get(c2).trim())
                .findFirst()
                .orElse(null);
    }

    protected List<String> getAllFromA(int a) {
        return read().stream().map(line -> line.get(a)).sorted().toList();
    }
}