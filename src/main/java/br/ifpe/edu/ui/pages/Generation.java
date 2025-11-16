package br.ifpe.edu.ui.pages;

import br.ifpe.edu.replacers.MatrixReplacer;
import br.ifpe.edu.replacers.PlaceholderReplacer;
import br.ifpe.edu.ui.PagesList;
import br.ifpe.edu.ui.common.Page;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

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










        Path filePath = Paths.get(
                Objects.requireNonNull(
                        Thread.currentThread().getContextClassLoader().getResource("ppc.docx")
                ).getPath()
        );

        try {
            var replacer = new PlaceholderReplacer(filePath, Paths.get(System.getProperty("user.dir"), "ppc.docx"));
            var matrixReplacer = new MatrixReplacer(Paths.get(System.getProperty("user.dir"), "ppc.docx"));

            replacer.replace();
            matrixReplacer.replace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JOptionPane.showMessageDialog(this, "Documento gerado com sucesso!");
    }
}
