package br.ifpe.edu.readers;

import lombok.AllArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.List;


public class CNCTReader extends AbstractCSVReader {

    @AllArgsConstructor
    private enum Columns {
        AXIS(0),
        NAME(2),
        MINIMUM_SUM_OF_CREDIT_HOURS(5);

        private final int index;
    }

    public CNCTReader() {
        super(
                "catalogo_cncst_2024.csv",
                StandardCharsets.ISO_8859_1,
                ';'
        );
    }

    public List<String> getAllNames() {
        return getAllFromA(Columns.NAME.index);
    }

    public String getHoursByName(String name) {
        return getAFromB(
                        Columns.NAME.index, name, Columns.MINIMUM_SUM_OF_CREDIT_HOURS.index
                ).split(" ")[0].replaceAll("\\D+", "");

    }

    public String getAxisByName(String name) {
        return getAFromB(Columns.NAME.index, name, Columns.AXIS.index);
    }
}
