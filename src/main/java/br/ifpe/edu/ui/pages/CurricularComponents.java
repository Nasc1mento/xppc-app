package br.ifpe.edu.ui.pages;

import br.ifpe.edu.CurricularComponentList;
import br.ifpe.edu.Eval;
import br.ifpe.edu.ui.common.ComboBox;
import br.ifpe.edu.ui.common.Page;
import br.ifpe.edu.ui.common.TextField;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class CurricularComponents extends Page {
    public enum CCType {
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

        public static CCType findByString(String value) {
            for (var cc : CCType.values()) {
                if (Objects.equals(cc.toString(), value)) {
                    return cc;
                }
            }

            return null;
        }
    }


    private final TextField codeField = new TextField(15);
    private final TextField ccField = new TextField(30);
    private final TextField periodField = new TextField(10).setInteger();
    private final TextField creditsField = new TextField(10).setInteger();
    private final TextField hrField = new TextField(10).setDouble();
    private final TextField extField = new TextField(10).setDouble();
    private final ComboBox<CCType> typeBox = new ComboBox<>(CCType.values());
    private final ComboBox<String> prereqBox = new ComboBox<>();
    private final ComboBox<String> coreqBox = new ComboBox<>();
    private final JButton addButton = new JButton("Adicionar");

    private final DefaultTableModel tableModel = new DefaultTableModel(new String[]{
            "Código",
            "Componentes Curriculares",
            "Tipo",
            "Período",
            "Créditos",
            "Total de Horas (H/A)",
            "Total de Horas (H/R)",
            "Total de Horas (H/R EXT)",
            "Pré-requisitos",
            "Correquisitos"
    }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(tableModel);

    private final CurricularComponentList curricularComponentList = CurricularComponentList.INSTANCE;

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
        addRow(new JLabel("Total de Horas (H/R): "), hrField);
        addRow(new JLabel("Total de Horas (H/R EXT): "), extField);
        addRow(new JLabel("Tipo: "),  typeBox);
        addRow(new JLabel("Período: "), periodField);
        addRow(new JLabel("Pré-requisitos"), prereqBox);
        addRow(new JLabel("Correquisitos"), coreqBox);
        addRow(addButton);
    }


    private void setupListeners() {
        addButton.addActionListener(_ -> addComponent());

        var deleteAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                deleteSelectedRow();
            }
        };

        InputMap inputMap = table.getInputMap(JTable.WHEN_FOCUSED);
        KeyStroke deleteKey = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
        inputMap.put(deleteKey, "deleteRow");
        table.getActionMap().put("deleteRow", deleteAction);
    }

    private void addComponent() {
        String code = codeField.getText();
        String name = ccField.getText();
        String type = Objects.toString(typeBox.getSelectedItem());
        String period = periodField.getText();
        String credits = creditsField.getText();
        String ha;
        String hr = hrField.getText();
        String ext = extField.getText();
        String prereq = (String) prereqBox.getSelectedItem();
        String coreq = (String) coreqBox.getSelectedItem();

        ha = Eval.eval(String.format("((%s+%s)*60)/45", hr, ext));

        tableModel.addRow(new Object[]{
                code,
                name,
                type,
                period,
                credits,
                ha,
                hr,
                ext,
                prereq,
                coreq
        });

        CurricularComponentList.CC novoComponente = new CurricularComponentList.CC(
                code,
                name,
                CCType.findByString(type),
                period,
                credits,
                ha,
                hr,
                ext,
                prereq,
                coreq
        );
        curricularComponentList.add(novoComponente);

        codeField.setText("");
        ccField.setText("");
        creditsField.setText("");
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

    private void deleteSelectedRow() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow >= 0) {
            String nameToDelete = (String) tableModel.getValueAt(selectedRow, 1);
            tableModel.removeRow(selectedRow);
            curricularComponentList.remove(nameToDelete);
            updatePrereqCoreqBoxes();
        }
    }

    @Override
    public String getTitle() {
        return "Informações dos Componentes Curriculares";
    }

    @Override
    public void onSubmit() {
        for (var c : curricularComponentList.getList()) {
            IO.println(c.name());
        }
    }
}