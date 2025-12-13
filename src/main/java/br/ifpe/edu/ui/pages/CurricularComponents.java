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

public class CurricularComponents extends Page implements IValidatable, ISubmittable {

    private final PlaceholderList placeholderList = PlaceholderList.INSTANCE;

    private final TextField codeField = new TextField(15);
    private final TextField ccField = new TextField(30);
    private final TextField periodField = new TextField(10).setInteger();
    private final TextField apField = new TextField(10).setInteger();
    private final TextField atField = new TextField(10).setInteger();
    private final TextField aeField = new TextField(10).setInteger();
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
        addRow(new JLabel("Créditos de Aulas Práticas: "), apField);
        addRow(new JLabel("Créditos de Aulas Teóricas: "), atField);
        addRow(new JLabel("Créditos de Extensão: "), aeField);
        addRow(new JLabel("Total de Horas Práticas (H/R): "), hrPrField);
        addRow(new JLabel("Total de Horas Teóricas (H/R): "), hrTeoField);
        addRow(new JLabel("Total de Horas de Extensão (H/R): "), extField);
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
                .bind(apField)
                .bind(atField)
                .bind(aeField)
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
        String actionKey = "deleteRow";
        inputMap.put(deleteKey, actionKey);
        table.getActionMap().put(actionKey, deleteAction);
    }

    private void addComponent() {
        String code = codeField.getText();
        String name = ccField.getText();
        String type = Objects.toString(typeBox.getSelectedItem());
        String period = periodField.getText();
        String ap = apField.getText();
        String at = atField.getText();
        String ae = aeField.getText();
        String hrPr = hrPrField.getText();
        String hrTeo = hrTeoField.getText();
        String ext = extField.getText();
        String prereq = (String) prereqBox.getSelectedItem();
        String coreq = (String) coreqBox.getSelectedItem();

        CC newCC = new CC(
                code,
                name,
                CCType.findByString(type),
                period,
                ap,
                at,
                ae,
                hrPr,
                hrTeo,
                ext,
                prereq,
                coreq
        );

        tableModel.addRow(new Object[]{
                code,
                name,
                type,
                period,
                newCC.credits(),
                newCC.ha(),
                newCC.hr(),
                ext,
                ext,
                prereq,
                coreq
        });


        ccList.add(newCC);

        clearFields();
        updatePrereqCoreqBoxes();
    }

    private void clearFields() {
        codeField.clear();
        apField.clear();
        atField.clear();
        aeField.clear();
        ccField.clear();
        hrPrField.clear();
        hrTeoField.clear();
        extField.clear();
        periodField.clear();
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
        var sumMandatoryTotal = Eval.evalDecimal("%s+%s",  sumMandatory.totalHr, sumMandatory.totalExt);

        var sumOptional = ccList.getSum(ccList.getList(), CCType.OPTIONAL);
        var sumOptionalTotal = Eval.evalDecimal("%s+%s",  sumOptional.totalHr, sumOptional.totalExt);

        var ca = placeholderList.getValue("carga_horaria_atividades_complementares_hr");
        var internship = placeholderList.getValue("carga_horaria_estagio_supervisionado_hr");

        placeholderList.addPlaceholder("ch_obrigatorios_hr", sumMandatoryTotal);
        placeholderList.addPlaceholder("ch_optativos_hr", sumOptionalTotal);

        var totalSum = Eval.evalDecimal("%s+%s+%s", ca, sumOptionalTotal, sumMandatoryTotal);
        placeholderList.addPlaceholder("cht",totalSum);
        placeholderList.addPlaceholder("cht_ha", Eval.evalDecimal("(%s*60)/45", totalSum));
        var trueTotal =  Eval.evalDecimal("%s+%s", totalSum, internship);
        placeholderList.addPlaceholder("cht_e_estagio", trueTotal);

        var sumMandatoryPer = Eval.evalDecimal("%s*100/%s", sumMandatoryTotal, trueTotal);
        var sumOptionalPer = Eval.evalDecimal("%s*100/%s", sumOptionalTotal, trueTotal);
        var caPer = Eval.evalDecimal("%s*100/%s", ca, trueTotal);
        var internshipPer = Eval.evalDecimal("%s*100/%s", internship, trueTotal);

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

        if (CourseLevel.TECHNOLOGIST.equals(CourseLevel.findByString(typeCourse))) {
            String courseName = placeholderList.getValue("nome_do_curso");
            String recommendedCht = cnctReader.getHoursByName(courseName);
            if (!Eval.compare(totalCht, recommendedCht)) {
                return JOptionPane.showConfirmDialog(
                        this,
                        String.format(
                            "<html>"
                            + "A carga horária do curso <b>%s</b> está <b>abaixo</b> da recomendada pelo "
                            + "Catálogo Nacional dos Cursos Tecnólogos.<hr>"
                            + "<b>Carga horária atual:</b> %s horas<br>"
                            + "<b>Carga horária recomendada:</b> %s horas<br><br>"
                            + "Deseja continuar mesmo assim?"
                            + "</html>",
                                courseName, totalCht, recommendedCht
                        ),
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