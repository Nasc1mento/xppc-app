package br.edu.ifpe.readers;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PeopleReader extends AbstractCSVReader {

    public static final PeopleReader INSTANCE = new PeopleReader();

    @AllArgsConstructor
    @Getter
    private enum Columns {
        NAME(0),
        ROLE(1);

        private final int index;
    }

    public record Person(
        String name,
        String role
    ) {  }

    private PeopleReader() {
        super("pessoas.csv", StandardCharsets.UTF_8, ',');
    }

    public List<Person> get() {
        final List<Person> people = new ArrayList<>();
        for (var line : read()) {
            var p = new Person(line.get(Columns.NAME.index), line.get(Columns.ROLE.index));
            people.add(p);
        }

        return people;
    }
}
