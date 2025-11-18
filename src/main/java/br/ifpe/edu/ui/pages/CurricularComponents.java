package br.ifpe.edu.ui.pages;

import br.ifpe.edu.ui.common.ComboBox;
import br.ifpe.edu.ui.common.Page;
import br.ifpe.edu.ui.common.TextField;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CurricularComponents extends Page {


    private enum CCType {
        MANDATORY("Obrigatória"),
        OPTIONAL("Optativa"),
        ELECTIVE("Eletiva");

        private final String s;

        CCType(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }
    }


    private final TextField codeField = new TextField(15);
    private final TextField ccField = new TextField(30);
    private final TextField creditsField = new TextField(10);
    private final TextField haField = new TextField(10);
    private final TextField hrField = new TextField(10);
    private final TextField extField = new TextField(10);
    private final ComboBox<CCType> typeBox = new ComboBox<>(CCType.values());
    private final ComboBox<String> prereqBox = new ComboBox<>();
    private final ComboBox<String> coreqBox = new ComboBox<>();
    private final JButton addButton = new JButton("Adicionar");


    private final DefaultTableModel tableModel = new DefaultTableModel(new String[]{
            "Código",
            "Componentes Curriculares",
            "Créditos",
            "Total de Horas (H/A)",
            "Total de Horas (H/R)",
            "Total de Horas (H/R EXT)",
            "Pré-requisitos",
            "Correquisitos"
    }, 0);

    private final JTable table = new JTable(tableModel);

    public  CurricularComponents() {
        setupLayout();
        setupListeners();
    }

    private void setupLayout() {
        var d = new Dimension(220, 25);
        coreqBox.setPreferredSize(d);
        prereqBox.setPreferredSize(d);

        addTable(table);
        addRow(new JLabel("Código: "), codeField);
        addRow(new JLabel("Nome do Componente Curricular"), ccField);
        addRow(new JLabel("Créditos: "), creditsField);
        addRow(new JLabel("Total de Horas (H/A): "), haField);
        addRow(new JLabel("Total de Horas (H/R): "), hrField);
        addRow(new JLabel("Total de Horas (H/R EXT): "), extField);
        addRow(new JLabel("Tipo: "),  typeBox);
        addRow(new JLabel("Pré-requisitos"), prereqBox);
        addRow(new JLabel("Correquisitos"), coreqBox);
        addRow(addButton);
    }


    private void setupListeners() {
        addButton.addActionListener(_ -> addComponent());
    }

    private void addComponent() {
        String codigo = codeField.getText();
        String nome = ccField.getText();
        String creditos = creditsField.getText();
        String ha = haField.getText();
        String hr = hrField.getText();
        String ext = extField.getText();
        String prereq = (String) prereqBox.getSelectedItem();
        String coreq = (String) coreqBox.getSelectedItem();

        tableModel.addRow(new Object[]{
                codigo,
                nome,
                creditos,
                ha,
                hr,
                ext,
                prereq,
                coreq
        });

        codeField.setText("");
        ccField.setText("");
        creditsField.setText("");
        haField.setText("");
        hrField.setText("");
        extField.setText("");
        updatePrereqCoreqBoxes();
    }

    private void updatePrereqCoreqBoxes() {
        prereqBox.removeAllItems();
        coreqBox.removeAllItems();

        int columnIndex = 1;

        for (int row = 0; row < tableModel.getRowCount(); row++) {
            Object value = tableModel.getValueAt(row, columnIndex);
            if (value != null) {
                String nome = value.toString();
                prereqBox.addItem(nome);
                coreqBox.addItem(nome);
            }
        }

        prereqBox.setSelectedIndex(-1);
        coreqBox.setSelectedIndex(-1);
    }

    @Override
    public String getTitle() {
        return "Informações dos Componentes Curriculares";
    }

    @Override
    public void onSubmit() {

    }
}
