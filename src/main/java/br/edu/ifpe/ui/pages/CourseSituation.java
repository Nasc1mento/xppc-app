package br.edu.ifpe.ui.pages;

import br.edu.ifpe.core.PlaceholderManager;
import br.edu.ifpe.ui.components.ISubmittable;
import br.edu.ifpe.ui.components.LabeledEnumComboBox;
import br.edu.ifpe.ui.components.Page;
import br.edu.ifpe.ui.components.TextField;
import br.edu.ifpe.enums.Situation;
import br.edu.ifpe.enums.Status;

import javax.swing.*;
import java.util.List;

public class CourseSituation extends Page implements ISubmittable {

    private final TextField cc = new TextField(10);
    private final TextField cpc = new TextField(10);
    private final TextField enade = new TextField(10);
    private final TextField igc = new TextField(10);
    private final LabeledEnumComboBox<Situation> situationBox = new LabeledEnumComboBox<>(Situation.class);
    private final LabeledEnumComboBox<Status> statusBox = new LabeledEnumComboBox<>(Status.class);

    private final PlaceholderManager placeholderManager = PlaceholderManager.INSTANCE;

    public CourseSituation() {
        setupForm();
    }


    private void setupForm() {
        addRow(new JLabel("Conceito de Curso (CC): "), cc);
        addRow(new JLabel("Conceito Preliminar de Curso (CPC): "), cpc);
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
        placeholderManager.addPlaceholder("cc", cc.getText());
        placeholderManager.addPlaceholder("cpc", cpc.getText());
        placeholderManager.addPlaceholder("enade", enade.getText());
        placeholderManager.addPlaceholder("igc", igc.getText());
        placeholderManager.addPrefersPlaceholder(
                List.of(
                        "apresentacao_inicial_do_ppc",
                        "reformulacao_integral_do_ppc",
                        "reformulacao_parcial_do_ppc"
                ), situationBox.getSelectedIndex()
        );

        placeholderManager.addPrefersPlaceholder(
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
