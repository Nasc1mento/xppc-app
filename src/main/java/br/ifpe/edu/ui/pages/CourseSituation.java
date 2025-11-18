package br.ifpe.edu.ui.pages;

import br.ifpe.edu.PlaceholderList;
import br.ifpe.edu.ui.common.ComboBox;
import br.ifpe.edu.ui.common.Page;
import br.ifpe.edu.ui.common.TextField;

import javax.swing.*;
import java.util.List;

public class CourseSituation extends Page {

    private enum Situation {
        OPTION1("Apresentação Inicial do PPC"),
        OPTION2("Reformulação Integral do PPC"),
        OPTION3("Reformulação Parcial do PPC");

        private final String s;

        Situation(final String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }
    }

    private enum Status {
        OPTION1("Aguardando autorização do conselho superior"),
        OPTION2("Autorizado pelo conselho superior"),
        OPTION3("Aguardando reconhecimento do MEC"),
        OPTION4("Reconhecido pelo MEC"),
        OPTION5("Cadastrado no SISTEC");

        private final String s;

        Status(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }
    }

    private final TextField cc = new TextField(10);
    private final TextField cpc = new TextField(10);
    private final TextField enade = new TextField(10);
    private final TextField igc = new TextField(10);
    private final ComboBox<Situation> situationBox = new ComboBox<>(Situation.values());
    private final ComboBox<Status> statusBox = new ComboBox<>(Status.values());

    private final PlaceholderList placeholderList = PlaceholderList.INSTANCE;

    public CourseSituation() {
        setupForm();
    }


    private void setupForm() {
        addRow(new JLabel("Conceito de Curso (CC): "), cc);
        addRow(new JLabel("Conceito Preliminar de Curso (CPC)"), cpc);
        addRow(new JLabel("Conceito Enade: "), enade);
        addRow(new JLabel("Índice Geral de Cursos (IGC): "), igc);
        addRow(new JLabel("Situação do Curso: "),  situationBox);
        addRow(new JLabel("Status do Curso: "), statusBox);
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
        placeholderList.addPrefersPlaceholder(
                List.of(
                        "apresentacao_inicial_do_ppc",
                        "reformulacao_integral_do_ppc",
                        "reformulacao_parcial_do_ppc"
                ), situationBox.getSelectedIndex()
        );

        placeholderList.addPrefersPlaceholder(
                List.of(
                    "aguardando_autorizacao_cs",
                    "autorizado_cs",
                    "aguardando_reconhecimento_mec",
                    "reconhecido_mec",
                    "cadastrado_sistec"
                ), statusBox.getSelectedIndex()
        );
    }
}
