package br.edu.ifpe.readers;

import org.apache.commons.csv.CSVRecord;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class PeopleReader extends AbstractCSVReader {

    public static final PeopleReader INSTANCE = new PeopleReader();

    private enum Columns {
        NAME(0),
        ROLE(1);

        private final int index;

        Columns(int index) {
            this.index = index;
        }
    }

    public record Person(
        String name,
        String role
    ) {
        public static Person fromRecord(CSVRecord row) {
            return new Person(
              row.get(Columns.NAME.index),
              row.get(Columns.ROLE.index)
            );
        }
    }

    private PeopleReader() {
        super("csv/pessoas.csv", StandardCharsets.UTF_8, ',');
    }

    public List<Person> getAll() {
        return read()
                .stream()
                .map(Person::fromRecord)
                .toList();
    }
}
