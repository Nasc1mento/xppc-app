package br.ifpe.edu.ui.pages;

import br.ifpe.edu.Eval;
import br.ifpe.edu.PlaceholderList;
import br.ifpe.edu.readers.CNCTReader;
import br.ifpe.edu.ui.common.ComboBox;
import br.ifpe.edu.ui.common.Page;
import br.ifpe.edu.ui.common.TextField;
import br.ifpe.edu.ui.models.CourseLevel;
import br.ifpe.edu.ui.models.CourseModality;
import br.ifpe.edu.ui.models.CourseRegime;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Course extends Page {

    private final ComboBox<CourseLevel> levelBox = new ComboBox<>(CourseLevel.values());
    private final TextField axisBox = new TextField(30);
    private final ComboBox<String> nameBox = new ComboBox<>();
    private final ComboBox<CourseModality> modalityBox = new ComboBox<>(CourseModality.values());
    private final TextField offersField = new TextField(30);
    private final TextField certificationField = new TextField(30);
    private final TextField internshipHoursField = new TextField(30).setDouble();
    private final TextField weeksField = new TextField(10).setInteger();
    private final TextField extraActivitiesHoursField = new TextField(30).setDouble();
    private final TextField minCompletionField = new TextField(10).setInteger();
    private final TextField maxCompletionField = new TextField(10).setInteger();
    private final TextField entryMethodsField = new TextField(30);
    private final TextField prereqField = new TextField(30);
    private final ComboBox<CourseRegime> regimeBox = new ComboBox<>(CourseRegime.values());
    private final TextField shiftsField = new TextField(10).setDouble();
    private final TextField classesPerShiftField = new TextField(10).setDouble();
    private final TextField seatsPerClassField = new TextField(10).setDouble();
    private final TextField seatsPerShiftField = new TextField(10).setDouble();
    private final TextField seatsPerSemesterField = new TextField(10).setInteger();
    private final TextField startField = new TextField(15);
    private final TextField durationField = new TextField(10).setDouble();

    private final CNCTReader cnctReader = new CNCTReader();
    private final PlaceholderList placeholderList = PlaceholderList.INSTANCE;

    public Course() {
        setupListeners();
        setupFields();
        setupComboBoxes();
        setupForm();
    }

    private void setupForm() {
        addRow(new JLabel("Tipo do curso: "), levelBox);
        addRow(new JLabel("Nome do curso: "), nameBox);
        addRow(new JLabel("Eixo tecnológico"), axisBox);
        addRow(new JLabel("Modalidade: "), modalityBox);
        addRow(new JLabel("Formas de oferta"), offersField);
        addRow(new JLabel("Titulação: "), certificationField);
        addRow(new JLabel("CH estágio supervisionado (H/R): "), internshipHoursField);
        addRow(new JLabel("Número de semanas letivas: "), weeksField);
        addRow(new JLabel("Atividades Complementares (H/R): "),  extraActivitiesHoursField);
        addRow(new JLabel("Período de integralização mínima(semestres e anos)"), minCompletionField);
        addRow(new JLabel("Período de integralização máxima (semestre e anos)"), maxCompletionField);
        addRow(new JLabel("Formas de acesso"), entryMethodsField);
        addRow(new JLabel("Pré-requisito para ingresso"), prereqField);
        addRow(new JLabel("Regime: "), regimeBox);
        addRow(new JLabel("Turnos: "), shiftsField);
        addRow(new JLabel("Número de turmas por turno de oferta: "), classesPerShiftField);
        addRow(new JLabel("Vagas por turma: "), seatsPerClassField);
        addRow(new JLabel("Número de vagas por turno de oferta: "), seatsPerShiftField);
        addRow(new JLabel("Vagas por semestre: "), seatsPerSemesterField);
        addRow(new JLabel("Duração do Curso: "), durationField);
        addRow(new JLabel("Início do Curso/ Matriz Curricular"),  startField);
    }

    private void setupListeners() {
        levelBox.addActionListener(_ -> {
            var selectedLevel = (CourseLevel) levelBox.getSelectedItem();

            nameBox.removeAllItems();

            if (CourseLevel.TECHNOLOGIST.equals(selectedLevel)) {
                nameBox.addAll(cnctReader.getAllNames());
            }
        });

        nameBox.addActionListener(_ -> {
            CourseLevel selectedLevel = (CourseLevel) levelBox.getSelectedItem();
            String selectedName = (String) nameBox.getSelectedItem();


            if (CourseLevel.TECHNOLOGIST.equals(selectedLevel) && Objects.nonNull(selectedName)) {
                axisBox.setText(cnctReader.getAxisByName(selectedName));
           }
        });
    }

    private void setupFields() {
        startField.setPreferredSize(new Dimension(125, 25));
    }

    private void setupComboBoxes() {
        nameBox.setEditable(true);
        axisBox.setEditable(true);

        nameBox.setPreferredSize(new Dimension(300, 25));
        axisBox.setPreferredSize(new Dimension(300, 25));
    }

    @Override
    public String getTitle() {
        return "Informações do Curso";
    }

    @Override
    public void onSubmit() {
        placeholderList.addPlaceholder("nome_do_curso", Objects.toString(nameBox.getSelectedItem(), ""));
        placeholderList.addPlaceholder("eixo_tecnologico", Objects.toString(axisBox.getText()));
        placeholderList.addPlaceholder("nivel", Objects.toString(levelBox.getSelectedItem()));
        placeholderList.addPlaceholder("modalidade", Objects.toString(modalityBox.getSelectedItem()));
        placeholderList.addPlaceholder("formas_de_oferta", offersField.getText());
        placeholderList.addPlaceholder("titulacao", certificationField.getText());
        placeholderList.addPlaceholder("carga_horaria_estagio_supervisionado_hr", internshipHoursField.getText());
        placeholderList.addPlaceholder("semanas_letivas", weeksField.getText());
        placeholderList.addPlaceholder("carga_horaria_atividades_complementares_hr", extraActivitiesHoursField.getText());
        placeholderList.addPlaceholder("integralizacao_minima", minCompletionField.getText());
        placeholderList.addPlaceholder("integralizacao_maxima", maxCompletionField.getText());
        placeholderList.addPlaceholder("forma_de_acesso", entryMethodsField.getText());
        placeholderList.addPlaceholder("pre-requisito_ingresso", prereqField.getText());
        placeholderList.addPlaceholder("regime", Objects.toString(regimeBox.getSelectedItem(), ""));
        placeholderList.addPlaceholder("turnos", shiftsField.getText());
        placeholderList.addPlaceholder("turmas_por_turno", classesPerShiftField.getText());
        placeholderList.addPlaceholder("vagas_por_turma", seatsPerClassField.getText());
        placeholderList.addPlaceholder("vagas_por_turno", seatsPerShiftField.getText());
        placeholderList.addPlaceholder("vagas_por_semestre", seatsPerSemesterField.getText());
        placeholderList.addPlaceholder("duracao",durationField.getText());
        placeholderList.addPlaceholder("inicio_do_curso", startField.getText());
        placeholderList.addPlaceholder("vagas_anuais", Eval.eval(seatsPerSemesterField.getText() + "*2"));
    }
}
