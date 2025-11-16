package br.ifpe.edu.ui.pages;

import br.ifpe.edu.PlaceholderList;
import br.ifpe.edu.readers.CNCTReader;
import br.ifpe.edu.ui.common.Page;
import br.ifpe.edu.ui.common.TextField;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Objects;

public class Course extends Page {

    public enum CourseRegime {
        FULL_TIME("Integral"),
        PART_TIME("Parcial");

        private final String s;

        CourseRegime(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }
    }


    private enum CourseModality {
        ON_SITE("Presencial"),
        HYBRID("Semipresencial"),
        ONLINE("Educação à distância");

        private final String s;

        CourseModality(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }
    }

    private enum CourseLevel {
        BACHELOR("Bacharelado"),
        TECHNOLOGIST("Superior de Tecnologia");

        private final String s;

        CourseLevel(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }
    }

    private final JComboBox<CourseLevel> levelBox = new JComboBox<>();
    private final JComboBox<String> axisBox = new JComboBox<>();
    private final JComboBox<String> nameBox = new JComboBox<>();
    private final JComboBox<CourseModality> modalityBox = new JComboBox<>();
    private final TextField certificationField = new TextField(30);
    private final TextField internshipHoursField = new TextField(30);
    private final TextField weeksField = new TextField(10);
    private final TextField extraActivitiesHoursField = new TextField(30);
    private final TextField minCompletionField = new TextField(10);
    private final TextField maxCompletionField = new TextField(10);
    private final TextField entryMethods = new TextField(30);
    private final TextField prereqField = new TextField(30);
    private final JComboBox<CourseRegime> regimeBox = new JComboBox<>();
    private final TextField shiftsField = new TextField(10);
    private final TextField classesPerShift = new TextField(10);
    private final TextField seatsPerClass = new TextField(10);
    private final TextField seatsPerShift = new TextField(10);
    private final TextField seatsPerSemester = new TextField(10);
    private final JFormattedTextField startField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));

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
        addRow(new JLabel("Titulação: "), certificationField);
        addRow(new JLabel("CH estágio supervisionado (H/R): "), internshipHoursField);
        addRow(new JLabel("Número de semanas letivas: "), weeksField);
        addRow(new JLabel("Atividades Complementares (H/R): "),  extraActivitiesHoursField);
        addRow(new JLabel("Período de integralização mínima(semestres e anos)"), minCompletionField);
        addRow(new JLabel("Período de integralização máxima (semestre e anos)"), maxCompletionField);
        addRow(new JLabel("Formas de acesso"), entryMethods);
        addRow(new JLabel("Pré-requisito para ingresso"), prereqField);
        addRow(new JLabel("Regime: "), regimeBox);
        addRow(new JLabel("Turnos: "), shiftsField);
        addRow(new JLabel("Número de turmas por turno de oferta: "), classesPerShift);
        addRow(new JLabel("Vagas por turma: "), seatsPerClass);
        addRow(new JLabel("Número de vagas por turno de oferta: "), seatsPerShift);
        addRow(new JLabel("Vagas por semestre: "), seatsPerSemester);
        addRow(new JLabel("Início do Curso/ Matriz Curricular"),  startField);
    }

    private void setupListeners() {
        levelBox.addActionListener(_ -> {
            var selectedLevel = (CourseLevel) levelBox.getSelectedItem();

            nameBox.removeAllItems();

            if (CourseLevel.TECHNOLOGIST.equals(selectedLevel)) {
                cnctReader.getAllNames().forEach(nameBox::addItem);
            }
        });

        nameBox.addActionListener(_ -> {
            CourseLevel selectedLevel = (CourseLevel) levelBox.getSelectedItem();
            String selectedName = (String) nameBox.getSelectedItem();

            axisBox.removeAllItems();

            if (CourseLevel.TECHNOLOGIST.equals(selectedLevel) && Objects.nonNull(selectedName)) {
                axisBox.setSelectedItem(cnctReader.getAxisByName(selectedName));
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

        Arrays.stream(CourseLevel.values()).forEach(levelBox::addItem);
        Arrays.stream(CourseModality.values()).forEach(modalityBox::addItem);
        Arrays.stream(CourseRegime.values()).forEach(regimeBox::addItem);
    }

    @Override
    public String getTitle() {
        return "Informações do Curso";
    }

    @Override
    public void onSubmit() {
        placeholderList.addPlaceholder("nome_do_curso", Objects.toString(nameBox.getSelectedItem()));
        placeholderList.addPlaceholder("nome_do_curso_maiusculo", Objects.toString(nameBox.getSelectedItem()).toUpperCase());
        placeholderList.addPlaceholder("eixo_tecnologico", Objects.toString(axisBox.getSelectedItem()));
        placeholderList.addPlaceholder("nivel", Objects.toString(levelBox.getSelectedItem()));
        placeholderList.addPlaceholder("modalidade", Objects.toString(modalityBox.getSelectedItem()));
        placeholderList.addPlaceholder("titulacao", certificationField.getText());
        placeholderList.addPlaceholder("carga_horaria_estagio_supervisionado_hr", internshipHoursField.getText());
        placeholderList.addPlaceholder("semanas_letivas", weeksField.getText());
        placeholderList.addPlaceholder("carga_horaria_atividades_complementares_hr", extraActivitiesHoursField.getText());
        placeholderList.addPlaceholder("integralizacao_minima", minCompletionField.getText());
        placeholderList.addPlaceholder("integralizacao_maxima", maxCompletionField.getText());
        placeholderList.addPlaceholder("formas_de_acesso", entryMethods.getText());
        placeholderList.addPlaceholder("pre-requisito_ingresso", prereqField.getText());
        placeholderList.addPlaceholder("regime", Objects.toString(regimeBox.getSelectedItem()));
        placeholderList.addPlaceholder("turnos", shiftsField.getText());
        placeholderList.addPlaceholder("turmas_por_turno", classesPerShift.getText());
        placeholderList.addPlaceholder("vagas_por_turma", seatsPerClass.getText());
        placeholderList.addPlaceholder("vagas_por_turno", seatsPerShift.getText());
        placeholderList.addPlaceholder("vagas_por_semestre", seatsPerSemester.getText());
        placeholderList.addPlaceholder("inicio_do_curso", Objects.toString(startField.getValue()));
    }
}
