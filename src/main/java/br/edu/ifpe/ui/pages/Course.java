package br.edu.ifpe.ui.pages;

import br.edu.ifpe.enums.CourseLevel;
import br.edu.ifpe.enums.CourseModality;
import br.edu.ifpe.enums.CourseRegime;
import br.edu.ifpe.readers.CNCTReader;
import br.edu.ifpe.core.PlaceholderManager;
import br.edu.ifpe.ui.components.*;
import br.edu.ifpe.ui.components.TextField;
import br.edu.ifpe.utils.Eval;

import javax.swing.*;
import java.awt.*;

public class Course extends Page implements ISubmittable {

    private final LabeledEnumComboBox<CourseLevel> levelBox = new LabeledEnumComboBox<>(CourseLevel.class);
    private final TextField axisField = new TextField(30);
    private final ComboBox<String> nameBox = new ComboBox<>();
    private final LabeledEnumComboBox<CourseModality> modalityBox = new LabeledEnumComboBox<>(CourseModality.class);
    private final TextField offersField = new TextField(30);
    private final TextField certificationField = new TextField(30);
    private final TextField internshipHoursField = new TextField(30).setDouble();
    private final TextField weeksField = new TextField(10).setInteger();
    private final TextField extraActivitiesHoursField = new TextField(30).setDouble();
    private final TextField minCompletionField = new TextField(10).setInteger();
    private final TextField maxCompletionField = new TextField(10).setInteger();
    private final TextField entryMethodsField = new TextField(30);
    private final TextField prereqField = new TextField(30);
    private final LabeledEnumComboBox<CourseRegime> regimeBox = new LabeledEnumComboBox<>(CourseRegime.class);
    private final TextField shiftsField = new TextField(30);
    private final TextField classesPerShiftField = new TextField(10).setInteger();
    private final TextField seatsPerClassField = new TextField(10).setInteger();
    private final TextField seatsPerShiftField = new TextField(10).setInteger();
    private final TextField seatsPerSemesterField = new TextField(10).setInteger();
    private final TextField startField = new TextField(15);
    private final TextField durationField = new TextField(10);
    private final CheckBox notInternshipCheckbox = new CheckBox("Não possui estágio supervisionado");

    private final CNCTReader cnctReader = CNCTReader.INSTANCE;
    private final PlaceholderManager placeholderManager = PlaceholderManager.INSTANCE;

    public Course() {
        setupListeners();
        setupFields();
        setupComboBoxes();
        setupForm();
    }

    private void setupForm() {
        addRow(new JLabel("Tipo do curso: "), levelBox);
        addRow(new JLabel("Nome do curso: "), nameBox);
        addRow(new JLabel("Eixo tecnológico: "), axisField);
        addRow(new JLabel("Modalidade: "), modalityBox);
        addRow(new JLabel("Formas de oferta: "), offersField);
        addRow(new JLabel("Titulação: "), certificationField);
        addRow(new JLabel("CH estágio supervisionado (H/R): "), internshipHoursField, notInternshipCheckbox);
        addRow(new JLabel("Número de semanas letivas: "), weeksField);
        addRow(new JLabel("Atividades Complementares (H/R): "),  extraActivitiesHoursField);
        addRow(new JLabel("Período de integralização mínima(em anos): "), minCompletionField);
        addRow(new JLabel("Período de integralização máxima (em anos): "), maxCompletionField);
        addRow(new JLabel("Formas de acesso: "), entryMethodsField);
        addRow(new JLabel("Pré-requisito para ingresso: "), prereqField);
        addRow(new JLabel("Regime: "), regimeBox);
        addRow(new JLabel("Turnos: "), shiftsField);
        addRow(new JLabel("Número de turmas por turno de oferta: "), classesPerShiftField);
        addRow(new JLabel("Vagas por turma: "), seatsPerClassField);
        addRow(new JLabel("Número de vagas por turno de oferta: "), seatsPerShiftField);
        addRow(new JLabel("Vagas por semestre: "), seatsPerSemesterField);
        addRow(new JLabel("Duração do Curso: "), durationField);
        addRow(new JLabel("Início do Curso/ Matriz Curricular: "),  startField);
    }

    private void setupListeners() {
        levelBox.addActionListener(_ -> {
            var selectedLevel = levelBox.getSelectedItem();

            nameBox.removeAllItems();
            axisField.clear();

            if (CourseLevel.TECHNOLOGIST.equals(selectedLevel)) {
                nameBox.addAll(cnctReader.getAllNames());
            }
        });

        nameBox.addActionListener(_ -> {
            CourseLevel selectedLevel = levelBox.getSelectedItem();
            String selectedName = nameBox.getSelectedItem();


            if (CourseLevel.TECHNOLOGIST.equals(selectedLevel) && selectedName != null) {
                axisField.setText(cnctReader.getAxisByName(selectedName));
           }
        });

        notInternshipCheckbox.addActionListener(_ -> {
            internshipHoursField.clear();
            internshipHoursField.setEnabled(!notInternshipCheckbox.isSelected());
        });
    }

    private void setupFields() {
        startField.setPreferredSize(new Dimension(125, 25));
    }

    private void setupComboBoxes() {
        nameBox.setEditable(true);
        axisField.setEditable(true);

        nameBox.setPreferredSize(new Dimension(300, 25));
        axisField.setPreferredSize(new Dimension(300, 25));
    }

    @Override
    public String getTitle() {
        return "Informações do Curso";
    }

    @Override
    public void onSubmit() {
        placeholderManager.addPlaceholder("nome_do_curso", nameBox.getStringValue());
        placeholderManager.addPlaceholder("eixo_tecnologico", axisField.getText());
        placeholderManager.addPlaceholder("nivel", levelBox.getStringValue());
        placeholderManager.addPlaceholder("modalidade", modalityBox.getStringValue());
        placeholderManager.addPlaceholder("formas_de_oferta", offersField.getText());
        placeholderManager.addPlaceholder("titulacao", certificationField.getText());
        placeholderManager.addPlaceholder("carga_horaria_estagio_supervisionado_hr", internshipHoursField.getText());
        placeholderManager.addPlaceholder("semanas_letivas", weeksField.getText());
        placeholderManager.addPlaceholder("carga_horaria_atividades_complementares_hr", extraActivitiesHoursField.getText());
        placeholderManager.addPlaceholder("integralizacao_minima", minCompletionField.getText());
        placeholderManager.addPlaceholder("integralizacao_maxima", maxCompletionField.getText());
        placeholderManager.addPlaceholder("forma_de_acesso", entryMethodsField.getText());
        placeholderManager.addPlaceholder("pre-requisito_ingresso", prereqField.getText());
        placeholderManager.addPlaceholder("regime", regimeBox.getStringValue());
        placeholderManager.addPlaceholder("turnos", shiftsField.getText());
        placeholderManager.addPlaceholder("turmas_por_turno", classesPerShiftField.getText());
        placeholderManager.addPlaceholder("vagas_por_turma", seatsPerClassField.getText());
        placeholderManager.addPlaceholder("vagas_por_turno", seatsPerShiftField.getText());
        placeholderManager.addPlaceholder("vagas_por_semestre", seatsPerSemesterField.getText());
        placeholderManager.addPlaceholder("duracao",durationField.getText());
        placeholderManager.addPlaceholder("inicio_do_curso", startField.getText());
        placeholderManager.addPlaceholder("vagas_anuais", Eval.evalDecimal("%s*2", seatsPerSemesterField.getText()));
    }
}
