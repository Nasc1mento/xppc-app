package br.ifpe.edu.readers;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractCSVReader {

    private final String fileName;
    private final Charset charset;
    private final CSVFormat csvFormat;
    private List<List<String>> list;

    protected AbstractCSVReader(String fileName, Charset charset, char separator) {
        this.fileName = fileName;
        this.charset = charset;

        this.csvFormat = CSVFormat.Builder.create()
                .setDelimiter(separator)
                .setIgnoreEmptyLines(true)
                .setHeader()
                .setSkipHeaderRecord(true)
                .setTrim(true)
                .get();
    }

    protected List<List<String>> read() {
        if (list == null) {
            list = new ArrayList<>();

            try (var reader = new InputStreamReader(
                    Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)),
                    charset
            )) {

                try (CSVParser parser = CSVParser.parse(reader, csvFormat)) {
                    for (CSVRecord record : parser) {
                        list.add(record.toList());
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return list;
    }

    protected String getAFromB(int c1, String b, int c2) {
        return read().stream()
                .filter(line -> line.size() > Math.max(c1, c2))
                .filter(line -> b.equals(line.get(c1)))
                .map(line -> line.get(c2).trim())
                .findFirst()
                .orElse(null);
    }

    protected List<String> getAllFromA(int a) {
        return read().stream()
                .map(line -> line.get(a))
                .sorted()
                .toList();
    }
}