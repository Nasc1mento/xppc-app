package br.ifpe.edu.readers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PeopleReader extends AbstractCSVReader {
    public static final int NAME_COLUMN = 0;
    public static final int ROLE_COLUMN = 1;

    public PeopleReader() {
        super("pessoas.csv", StandardCharsets.UTF_8, ',');
    }

    public List<String[]> get() {
        final List<String[]> people = new ArrayList<>();
        for (var line : super.read()) {
            var arr =  new String[2];
            arr[0] = line[NAME_COLUMN];
            arr[1] = line[ROLE_COLUMN];
            people.add(arr);
        }

        return people;
    }
}
