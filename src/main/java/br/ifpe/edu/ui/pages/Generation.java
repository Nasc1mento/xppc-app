package br.ifpe.edu.ui.pages;

import br.ifpe.edu.replacers.ReplacerList;
import br.ifpe.edu.ui.PagesList;
import br.ifpe.edu.ui.common.Page;

import javax.swing.*;

public class Generation extends Page {

    private final JButton generateButton = new JButton("Gerar Documento");

    public Generation() {
        setupListeners();
        setupForm();
    }

    private void setupListeners() {
        generateButton.addActionListener(_ -> PagesList.getList().forEach(Page::onSubmit));
    }

    private void setupForm() {
        add(generateButton);
    }

    @Override
    public String getTitle() {
        return "Gerar documento";
    }

    @Override
    public void onSubmit() {
        if (ReplacerList.callAll())
            JOptionPane.showMessageDialog(this, "Documento gerado com sucesso!");
        else
            JOptionPane.showMessageDialog(this, "Erro ao gerar Documento!");
    }
}
