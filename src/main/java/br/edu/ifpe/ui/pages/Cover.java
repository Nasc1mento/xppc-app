package br.edu.ifpe.ui.pages;

import br.edu.ifpe.config.AppConfig;
import br.edu.ifpe.readers.PeopleReader;
import br.edu.ifpe.ui.components.Page;

import javax.swing.*;
import java.awt.*;


public class Cover extends Page {
    private final JLabel titleLabel = new JLabel(AppConfig.getName());
    private final JButton aboutButton = new JButton();
    private final PeopleReader peopleReader = PeopleReader.INSTANCE;

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
                    .append("</p>")
                    .append("<br>")
                    .append("<p><b>Equipe</b></p>")
                    .append("<hr>")
                    .append("<ul>");

            for (var person : peopleReader.get()) {
                sb.append("<li><b>")
                        .append(person[PeopleReader.Columns.NAME.getIndex()])
                        .append("</b>: ")
                        .append(person[PeopleReader.Columns.ROLE.getIndex()])
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
        aboutButton.setContentAreaFilled(false);
        aboutButton.setFocusPainted(false);

        aboutButton.setIcon(UIManager.getIcon("OptionPane.questionIcon"));

        addRow(titleLabel, aboutButton);
    }


    @Override
    public String getTitle() {
        return "Bem-vindo(a)";
    }
}
