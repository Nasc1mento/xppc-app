package br.edu.ifpe.infra.storage.csv;

import org.apache.commons.csv.CSVRecord;

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


    public record Campus(
            String name,
            String cnpj,
            String city,
            String neighbourhood,
            String cep,
            String street,
            String number,
            String phone,
            String email,
            String aldc,
            String website,
            String history
    ) {
        public static Campus fromRecord(CSVRecord row) {
            return new Campus(
                    row.get(Columns.NAME.ordinal()),
                    row.get(Columns.CNPJ.ordinal()),
                    row.get(Columns.CITY.ordinal()),
                    row.get(Columns.NEIGHBOURHOOD.ordinal()),
                    row.get(Columns.CEP.ordinal()),
                    row.get(Columns.STREET.ordinal()),
                    row.get(Columns.NUMBER.ordinal()),
                    row.get(Columns.PHONE.ordinal()),
                    row.get(Columns.EMAIL.ordinal()),
                    row.get(Columns.ALDC.ordinal()),
                    row.get(Columns.WEBSITE.ordinal()),
                    row.get(Columns.HISTORY.ordinal())
            );
        }
    }

    private CampusReader() {
        super(
                "csv/campi.csv",
                StandardCharsets.UTF_8,
                ','
        );
    }

    public List<String> getAllNames() {
        return getAllFromA(Columns.NAME.ordinal());
    }

    public Campus getByName(final String name) {
        var record = getFirstFromA(Columns.NAME.ordinal(), name);
        return (record != null) ? Campus.fromRecord(record) : null;
    }
}
