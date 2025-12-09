package br.ifpe.edu.ui.pages;


import br.ifpe.edu.PlaceholderList;
import br.ifpe.edu.readers.CampusReader;
import br.ifpe.edu.ui.common.ComboBox;
import br.ifpe.edu.ui.common.ISubmittable;
import br.ifpe.edu.ui.common.Page;
import br.ifpe.edu.ui.common.TextField;

import javax.swing.*;
import java.awt.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;

public class Proponent extends Page implements ISubmittable {

    private final TextField cnpjField = new TextField(12);
    private final TextField cityField = new TextField(20);
    private final JTextField cepField = new JTextField(10);
    private final TextField neighbourhoodField = new TextField(20);
    private final TextField streetField = new TextField(20);
    private final TextField numberField = new TextField(20);
    private final TextField telephoneNumberField = new TextField(20);
    private final TextField emailField = new TextField(20);
    private final TextField aldcField = new TextField(25);
    private final TextField websiteField = new TextField(30);
    private final ComboBox<String> campusBox = new ComboBox<>();

    private final CampusReader campusReader = new CampusReader();
    private final PlaceholderList placeholderList = PlaceholderList.INSTANCE;

    public Proponent() {
        setupComboBoxes();
        setupForm();
        setupListeners();
    }

    private void setupComboBoxes() {
        cnpjField.setEnabled(false);
        campusBox.setPreferredSize(new Dimension(220, 25));

        campusBox.addAll(campusReader.getAllNames());
        campusBox.setSelectedItem(null);
    }

    private void setupListeners() {
        var fieldMapping = Map.of(
                cnpjField, CampusReader.Columns.CNPJ_COLUMN,
                cityField, CampusReader.Columns.CITY_COLUMN,
                cepField, CampusReader.Columns.CEP_COLUMN,
                neighbourhoodField, CampusReader.Columns.NEIGHBOURHOOD_COLUMN,
                streetField, CampusReader.Columns.STREET_COLUMN,
                numberField, CampusReader.Columns.NUMBER_COLUMN,
                telephoneNumberField, CampusReader.Columns.PHONE_COLUMN,
                emailField, CampusReader.Columns.EMAIL_COLUMN,
                aldcField, CampusReader.Columns.ALDC_COLUMN,
                websiteField, CampusReader.Columns.WEBSITE_COLUMN
        );
        campusBox.addActionListener(_ -> {
            String selectedCampus = (String) campusBox.getSelectedItem();
            if (selectedCampus != null) {
                fieldMapping.forEach((field, column) -> field.setText(campusReader.getByNameAndColumn(selectedCampus, column)));
            }
        });
    }

    private void setupForm() {
        addRow(new JLabel("Nome do Campus: "), campusBox);
        addRow(new JLabel("CNPJ: "), cnpjField);
        addRow(new JLabel("Cidade: "), cityField);
        addRow(new JLabel("CEP: "), cepField);
        addRow(new JLabel("Bairro: "), neighbourhoodField);
        addRow(new JLabel("Rua: "), streetField);
        addRow(new JLabel("Número: "), numberField);
        addRow(new JLabel("Telefone/Fax: "), telephoneNumberField);
        addRow(new JLabel("E-mail de contato: "), emailField);
        addRow(new JLabel("Ato Legal de Criação: "), aldcField);
        addRow( new JLabel("Sítio: "), websiteField);
    }

    @Override
    public String getTitle() {
        return "Informações da Proponente";
    }

    @Override
    public void onSubmit() {
        placeholderList.addPlaceholder("campus", Objects.toString(campusBox.getSelectedItem()));
        placeholderList.addPlaceholder("cnpj", cnpjField.getText());
        placeholderList.addPlaceholder("cidade", Objects.toString(cityField.getText()));
        placeholderList.addPlaceholder("cidade_uf_cep", String.format("%s, PE, %s, %s", cityField.getText(), neighbourhoodField.getText(), cepField.getText()));
        placeholderList.addPlaceholder("rua_numero", String.format("%s, %s", streetField.getText(), numberField.getText()));
        placeholderList.addPlaceholder("telefone", telephoneNumberField.getText());
        placeholderList.addPlaceholder("email", emailField.getText());
        placeholderList.addPlaceholder("ato_legal_de_criacao", aldcField.getText());
        placeholderList.addPlaceholder("sitio", websiteField.getText());
        placeholderList.addPlaceholder("ano", ZonedDateTime.now(ZoneId.of("America/Recife")).getYear());
    }
}
