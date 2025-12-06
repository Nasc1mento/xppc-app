package br.ifpe.edu.ui.pages;

import br.ifpe.edu.AppConfig;
import br.ifpe.edu.readers.PeopleReader;
import br.ifpe.edu.ui.common.Page;

import javax.swing.*;
import java.awt.*;

public class Cover extends Page {

    private final JLabel titleLabel = new JLabel("xPPC - Aplicação para Geração Automatizada de Projetos Pedagógicos de Curso Superiores do IFPE");
    private final JButton aboutButton = new JButton("Sobre");
    private final PeopleReader peopleReader = new PeopleReader();

    public Cover() {
        setupLayout();
        setupLabels();
        setupListener();
    }

    private void setupListener() {
        aboutButton.addActionListener(_ -> {
            var sb = new StringBuilder()
                    .append("<html>")
                    .append("<p><b>Versão:</b> ").append(AppConfig.getVersion()).append("</p>")
                    .append("<hr>")
                    .append("<p>").append(AppConfig.getName())
                    .append(" - Aplicação para Geração Automatizada de Projetos Pedagógicos de Curso Superiores do IFPE")
                    .append("</p>")
                    .append("<br>")
                    .append("<p><b>Equipe</b></p>")
                    .append("<hr>")
                    .append("<ul>");

            for (var person : peopleReader.get()) {
                sb.append("<li><b>")
                        .append(person[PeopleReader.NAME_COLUMN])
                        .append("</b>: ")
                        .append(person[PeopleReader.ROLE_COLUMN])
                        .append("</li>");
            }

            sb.append("</ul></html>");


            JOptionPane.showMessageDialog(
                    this,
                    sb.toString(),
                    "Sobre",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }

    private void setupLabels() {
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24f));
    }


    private void setupLayout() {
        addRow(titleLabel, GridBagConstraints.CENTER);
        addRow(aboutButton, GridBagConstraints.CENTER);
    }


    @Override
    public String getTitle() {
        return "Bem-vindo(a)";
    }
}
