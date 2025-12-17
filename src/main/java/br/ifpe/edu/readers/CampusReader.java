package br.ifpe.edu.readers;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class CampusReader extends AbstractCSVReader {

    public static final CampusReader INSTANCE = new CampusReader();

    public enum Columns {
        NAME(),
        CNPJ,
        CITY,
        NEIGHBOURHOOD,
        CEP,
        STREET,
        NUMBER,
        PHONE,
        EMAIL,
        ALDC,
        WEBSITE,
        HISTORY,
    }

    private CampusReader() {
        super(
                "campi.csv",
                StandardCharsets.UTF_8,
                ','
        );
    }

    public List<String> getAllNames() {
        return getAllFromA(Columns.NAME.ordinal());
    }

    public String getByNameAndColumn(final String name, final Columns column) {
        return getAFromB(Columns.NAME.ordinal(), name, column.ordinal());
    }
}
