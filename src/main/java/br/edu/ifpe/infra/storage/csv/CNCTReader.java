package br.edu.ifpe.infra.storage.csv;

import java.nio.charset.StandardCharsets;
import java.util.List;


public class CNCTReader extends AbstractCSVReader {

    public static final CNCTReader INSTANCE = new CNCTReader();

    private enum Columns {
        AXIS(0),
        NAME(2),
        MINIMUM_SUM_OF_CREDIT_HOURS(5);

        private final int index;

        Columns(int index) {
            this.index = index;
        }
    }

    private CNCTReader() {
        super(
                "csv/catalogo_cncst_2024.csv",
                StandardCharsets.ISO_8859_1,
                ';'
        );
    }

    public List<String> getAllNames() {
        return getAllFromA(Columns.NAME.index);
    }

    public String getHoursByName(final String name) {
        return getAFromB(Columns.NAME.index, name, Columns.MINIMUM_SUM_OF_CREDIT_HOURS.index)
                .split(" ")[0]
                .replaceAll("\\D+", "");

    }

    public String getAxisByName(final String name) {
        return getAFromB(Columns.NAME.index, name, Columns.AXIS.index);
    }
}
