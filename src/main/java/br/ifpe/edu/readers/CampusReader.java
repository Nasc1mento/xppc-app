package br.ifpe.edu.readers;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class CampusReader extends AbstractCSVReader {

    public enum Columns {
        NAME_COLUMN (),
        CNPJ_COLUMN,
        CITY_COLUMN,
        NEIGHBOURHOOD_COLUMN,
        CEP_COLUMN,
        STREET_COLUMN,
        NUMBER_COLUMN,
        PHONE_COLUMN,
        EMAIL_COLUMN,
        ALDC_COLUMN,
        WEBSITE_COLUMN
    }

    public CampusReader() {
        super(
                "campus.csv",
                StandardCharsets.UTF_8,
                ','
        );
    }

    public List<String> getAllNames() {
        return getAllFromA(Columns.NAME_COLUMN.ordinal());
    }

    public String getByNameAndColumn(String name, Columns column) {
        return getAFromB(Columns.NAME_COLUMN.ordinal(), name, column.ordinal());
    }
}
