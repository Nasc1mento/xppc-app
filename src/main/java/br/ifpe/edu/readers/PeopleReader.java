package br.ifpe.edu.readers;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PeopleReader extends AbstractCSVReader {

    @AllArgsConstructor
    @Getter
    public enum Columns {
        NAME(0),
        ROLE(1);

        private final int index;
    }

    public PeopleReader() {
        super("pessoas.csv", StandardCharsets.UTF_8, ',');
    }

    public List<String[]> get() {
        final List<String[]> people = new ArrayList<>();
        for (var line : read()) {
            var arr =  new String[2];
            arr[0] = line.get(Columns.NAME.index);
            arr[1] = line.get(Columns.ROLE.index);
            people.add(arr);
        }

        return people;
    }
}
