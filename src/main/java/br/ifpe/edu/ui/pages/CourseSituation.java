package br.ifpe.edu.ui.pages;

import br.ifpe.edu.PlaceholderList;
import br.ifpe.edu.ui.common.Page;
import br.ifpe.edu.ui.common.TextField;

import javax.swing.*;

public class CourseSituation extends Page {


    private final TextField cc = new TextField(10);
    private final TextField cpc = new TextField(10);
    private final TextField enade = new TextField(10);
    private final TextField igc = new TextField(10);

    private final PlaceholderList placeholderList = PlaceholderList.INSTANCE;

    public CourseSituation() {

        setupForm();
    }


    private void setupForm() {
        addRow(new JLabel("Conceito de Curso (CC): "), cc);
        addRow(new JLabel("Conceito Preliminar de Curso (CPC)"), cpc);
        addRow(new JLabel("Conceito Enade: "), enade);
        addRow(new JLabel("Índice Geral de Cursos (IGC): "), igc);
    }



    @Override
    public String getTitle() {
        return "Situação do Curso";
    }

    @Override
    public void onSubmit() {
        placeholderList.addPlaceholder("cc", cc.getText());
        placeholderList.addPlaceholder("cpc", cpc.getText());
        placeholderList.addPlaceholder("enade", enade.getText());
        placeholderList.addPlaceholder("igc", igc.getText());
    }
}
