package br.ifpe.edu.readers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PeopleReader extends AbstractCSVReader {

    public enum Columns {
        NAME(0),
        ROLE(1);


        private final int index;

        Columns(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    public PeopleReader() {
        super("pessoas.csv", StandardCharsets.UTF_8, ',');
    }

    public List<String[]> get() {
        final List<String[]> people = new ArrayList<>();
        for (var line : read()) {
            var arr =  new String[2];
            arr[0] = line[Columns.NAME.index];
            arr[1] = line[Columns.ROLE.index];
            people.add(arr);
        }

        return people;
    }
}
