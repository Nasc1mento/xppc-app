package br.ifpe.edu.readers;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class AbstractCSVReader {

    private final String fileName;
    private final Charset charset;
    private final char separator;

    protected AbstractCSVReader(String fileName, Charset charset, char separator) {
        this.fileName = fileName;
        this.charset = charset;
        this.separator = separator;
    }

    protected List<String[]> read() {
        try (var reader = new CSVReaderBuilder(
                new InputStreamReader(
                        Objects.requireNonNull(
                                Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)
                        ),
                        charset
                ))
                .withCSVParser(new CSVParserBuilder().withSeparator(separator).build())
                .build()) {

            var records = reader.readAll();

            return records.stream()
                    .skip(1)
                    .filter(line -> line != null && line.length > 1)
                    .filter(line -> !isEmptyLine(line))
                    .toList();

        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getAFromB(int c1, String b, int c2) {
        return read().stream()
                .filter(line -> line.length > Math.max(c1, c2))
                .filter(line -> line[c1].equals(b))
                .map(line -> line[c2].trim())
                .findFirst()
                .orElse(null);
    }

    protected List<String> getAllFromA(int a) {
        return read().stream()
                .map(line -> line[a])
                .sorted()
                .toList();
    }

    private boolean isEmptyLine(String[] l) {
        return Arrays.stream(l).noneMatch(s -> Objects.nonNull(s) && !s.isBlank());
    }
}
