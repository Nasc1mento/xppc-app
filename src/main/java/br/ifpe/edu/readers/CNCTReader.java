package br.ifpe.edu.readers;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class CNCTReader extends AbstractCSVReader {
    private static final int AXIS_COLUMN = 0;
    private static final int NAME_COLUMN = 2;
    private static final int MINIMUM_SUM_OF_CREDIT_HOURS_COLUMN = 5;

    public CNCTReader() {
        super(
                "catalogo_cncst_2024.csv",
                StandardCharsets.ISO_8859_1,
                ';'
        );
    }

    public List<String> getAllNames() {
        return getAllFromA(NAME_COLUMN);
    }

    public String getHoursByName(String name) {
        return getAFromB(
                        NAME_COLUMN, name, MINIMUM_SUM_OF_CREDIT_HOURS_COLUMN
                ).split(" ")[0].replaceAll("\\D+", "");

    }

    public String getAxisByName(String name) {
        return getAFromB(NAME_COLUMN, name, AXIS_COLUMN);
    }
}
