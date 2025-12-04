package br.ifpe.edu.ui.pages;

import br.ifpe.edu.replacers.ReplacerList;
import br.ifpe.edu.replacers.helpers.DocumentHelper;
import br.ifpe.edu.ui.PagesList;
import br.ifpe.edu.ui.common.Page;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;

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
        var chooser = new JFileChooser();
        chooser.setDialogTitle("Escolha onde quer salvar o documento");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = chooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDir = chooser.getSelectedFile();
            DocumentHelper.INSTANCE.setOutputPath(selectedDir.toPath().resolve("ppc.docx"));

            var rl = new ReplacerList();

            if (rl.callAll())
                JOptionPane.showMessageDialog(this, "Documento gerado com sucesso!");
            else
                JOptionPane.showMessageDialog(this, "Erro ao gerar Documento!");
        }
    }
}
