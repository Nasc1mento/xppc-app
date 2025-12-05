package br.ifpe.edu.ui.pages;

import br.ifpe.edu.CCList;
import br.ifpe.edu.Eval;
import br.ifpe.edu.PlaceholderList;
import br.ifpe.edu.readers.CNCTReader;
import br.ifpe.edu.ui.common.*;
import br.ifpe.edu.ui.common.TextField;
import br.ifpe.edu.ui.models.CC;
import br.ifpe.edu.ui.models.CCType;
import br.ifpe.edu.ui.models.CourseLevel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class CurricularComponents extends Page implements IValidatable {

    private final PlaceholderList placeholderList = PlaceholderList.INSTANCE;

    private final TextField codeField = new TextField(15);
    private final TextField ccField = new TextField(30);
    private final TextField periodField = new TextField(10).setInteger();
    private final TextField creditsField = new TextField(10).setInteger();
    private final TextField hrPrField = new TextField(10).setDouble();
    private final TextField hrTeoField = new TextField(10).setDouble();
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
            "Total de Horas Prática/Teórica (H/R)",
            "Total de Horas de Extensão (H/R)",
            "Pré-requisitos",
            "Correquisitos"
    }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(tableModel);

    private final CCList ccList = CCList.INSTANCE;

    public  CurricularComponents() {
        setupLayout();
        setupListeners();
    }

    private void setupLayout() {
        var d = new Dimension(220, 25);
        coreqBox.setPreferredSize(d);
        prereqBox.setPreferredSize(d);

        addButton.setEnabled(false);

        addTable(table);
        addRow(new JLabel("Código: "), codeField);
        addRow(new JLabel("Nome do Componente Curricular"), ccField);
        addRow(new JLabel("Créditos: "), creditsField);
        addRow(new JLabel("Total de Horas Práticas (H/R): "), hrPrField);
        addRow(new JLabel("Total de Horas Teóricas (H/R): "), hrTeoField);
        addRow(new JLabel("Total de Horas (H/R EXT): "), extField);
        addRow(new JLabel("Tipo: "),  typeBox);
        addRow(new JLabel("Período: "), periodField);
        addRow(new JLabel("Pré-requisitos"), prereqBox);
        addRow(new JLabel("Correquisitos"), coreqBox);
        addRow(addButton);
    }

    private void setupListeners() {
        addButton.addActionListener(_ -> addComponent());

        BindPropertyFactory.create()
                .bind(codeField)
                .bind(ccField)
                .bind(periodField)
                .bind(hrPrField)
                .bind(hrTeoField)
                .bind(extField)
                .bind(typeBox)
                .bind(periodField)
                .onChange(addButton::setEnabled);

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
        String hr = Eval.eval("%s+%s", hrPrField.getText(), hrTeoField.getText());
        String ext = extField.getText();
        String ha = Eval.eval("((%s+%s)*60)/45", hr, ext);
        String prereq = (String) prereqBox.getSelectedItem();
        String coreq = (String) coreqBox.getSelectedItem();

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

        CC newCC = new CC(
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
        ccList.add(newCC);

        codeField.setText("");
        ccField.setText("");
        creditsField.setText("");
        hrPrField.setText("");
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
            ccList.remove(nameToDelete);
            updatePrereqCoreqBoxes();
        }
    }

    @Override
    public String getTitle() {
        return "Informações dos Componentes Curriculares";
    }

    @Override
    public void onSubmit() {

        var sumMandatory = ccList.getSum(ccList.getList(), CCType.MANDATORY);
        var sumMandatoryTotal = Eval.eval("%s+%s",  sumMandatory.totalHr, sumMandatory.totalExt);

        var sumOptional = ccList.getSum(ccList.getList(), CCType.OPTIONAL);
        var sumOptionalTotal = Eval.eval("%s+%s",  sumOptional.totalHr, sumOptional.totalExt);

        var ca = placeholderList.getValue("carga_horaria_atividades_complementares_hr");
        var internship = placeholderList.getValue("carga_horaria_estagio_supervisionado_hr");

        placeholderList.addPlaceholder("ch_obrigatorios_hr", sumMandatoryTotal);
        placeholderList.addPlaceholder("ch_optativos_hr", sumOptionalTotal);

        var totalSum = Eval.eval("%s+%s+%s+%s", ca, sumOptionalTotal, sumMandatoryTotal, internship);
        placeholderList.addPlaceholder("cht",totalSum);
        placeholderList.addPlaceholder("cht_ha", Eval.eval("(%s*60)/45", totalSum));
        placeholderList.addPlaceholder("cht_e_estagio", Eval.eval("%s+%s", totalSum, internship));

        var sumMandatoryPer = Eval.eval("%s * 100 / %s", sumMandatoryTotal, totalSum);
        var sumOptionalPer = Eval.eval("%s * 100 / %s", sumOptionalTotal, totalSum);
        var caPer = Eval.eval("%s * 100 / %s", ca, totalSum);
        var internshipPer = Eval.eval("%s * 100 / %s", internship, totalSum);

        placeholderList.addPlaceholder("ch_obrigatorios_per", sumMandatoryPer);
        placeholderList.addPlaceholder("ch_optativos_per", sumOptionalPer);
        placeholderList.addPlaceholder("carga_horaria_estagio_supervisionado_per", internshipPer);
        placeholderList.addPlaceholder("carga_horaria_atividades_complementares_per", caPer);
    }
    
    @Override
    public int check() {
        final CNCTReader cnctReader = new CNCTReader();
        String totalCht = placeholderList.getValue("cht_e_estagio");
        String typeCourse =  placeholderList.getValue("nivel");

        IO.println(typeCourse);

        if (CourseLevel.TECHNOLOGIST.equals(CourseLevel.findByString(typeCourse))) {
            String recommendedCht = cnctReader.getHoursByName(placeholderList.getValue("nome_do_curso"));
            if (Eval.evalBoolean("%s < %s", totalCht, recommendedCht)) {
                return JOptionPane.showConfirmDialog(
                        this,
                        String.format("Carga horária abaixo da recomendada pelo CNCT: %s < %s",  totalCht, recommendedCht),
                        "Aviso",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
            }

            return JOptionPane.YES_OPTION;
        }


        return JOptionPane.YES_OPTION;
    }
}