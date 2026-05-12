package br.edu.ifpe.ui.swing.pages;


import br.edu.ifpe.core.PlaceholderManager;
import br.edu.ifpe.infra.storage.csv.CampusReader;
import br.edu.ifpe.ui.swing.components.ComboBox;
import br.edu.ifpe.ui.swing.components.ISubmittable;
import br.edu.ifpe.ui.swing.components.Page;
import br.edu.ifpe.ui.swing.components.TextField;

import javax.swing.*;
import java.awt.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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

    private final CampusReader campusReader = CampusReader.INSTANCE;
    private final PlaceholderManager placeholderManager = PlaceholderManager.INSTANCE;

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
        campusBox.addActionListener(_ -> {
            String selectedCampus = campusBox.getSelectedItem();
            if (selectedCampus != null) {
                var campus = campusReader.getByName(selectedCampus);
                if (campus != null) {
                    cnpjField.setText(campus.cnpj());
                    cityField.setText(campus.city());
                    cepField.setText(campus.cep());
                    neighbourhoodField.setText(campus.neighbourhood());
                    streetField.setText(campus.street());
                    numberField.setText(campus.number());
                    telephoneNumberField.setText(campus.phone());
                    emailField.setText(campus.email());
                    aldcField.setText(campus.aldc());
                    websiteField.setText(campus.website());
                }
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
        placeholderManager.addPlaceholder("campus", campusBox.getStringValue());
        placeholderManager.addPlaceholder("cnpj", cnpjField.getText());
        placeholderManager.addPlaceholder("cidade",cityField.getText());
        placeholderManager.addPlaceholder("cidade_uf_cep", String.format("%s, PE, %s, %s", cityField.getText(), neighbourhoodField.getText(), cepField.getText()));
        placeholderManager.addPlaceholder("rua_numero", String.format("%s, %s", streetField.getText(), numberField.getText()));
        placeholderManager.addPlaceholder("telefone", telephoneNumberField.getText());
        placeholderManager.addPlaceholder("email", emailField.getText());
        placeholderManager.addPlaceholder("ato_legal_de_criacao", aldcField.getText());
        placeholderManager.addPlaceholder("sitio", websiteField.getText());
        placeholderManager.addPlaceholder("ano", ZonedDateTime.now(ZoneId.of("America/Recife")).getYear());
    }
}
