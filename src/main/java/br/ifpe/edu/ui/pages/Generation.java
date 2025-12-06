package br.ifpe.edu.ui.pages;

import br.ifpe.edu.replacers.ReplacerList;
import br.ifpe.edu.replacers.helpers.DocumentPath;
import br.ifpe.edu.ui.PagesList;
import br.ifpe.edu.ui.common.ISubmittable;
import br.ifpe.edu.ui.common.IValidatable;
import br.ifpe.edu.ui.common.Page;

import javax.swing.*;
import java.io.File;
import java.nio.file.Paths;

public class Generation extends Page implements ISubmittable {

    private final JButton generateButton = new JButton("Gerar Documento");

    public Generation() {
        setupListeners();
        setupForm();
    }

    private void setupListeners() {
        generateButton.addActionListener(_ -> {
            for (var p : PagesList.getList()) {
                if (p instanceof ISubmittable) {
                    ((ISubmittable) p).onSubmit();
                }
            }
        });
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

        for (var p : PagesList.getList()) {
            if (p instanceof IValidatable) {
                int v = ((IValidatable) p).check();
                if (v != JOptionPane.YES_OPTION) {
                    return;
                }
            }
        }

        var chooser = new JFileChooser();
        chooser.setDialogTitle("Escolha onde quer salvar o documento");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = chooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDir = chooser.getSelectedFile();
            DocumentPath.INSTANCE.setOutputPath(Paths.get(Paths.get(selectedDir.toURI()).toString(), "ppc.docx"));

            try (var rl = new ReplacerList()) {
                rl.cAll();
                JOptionPane.showMessageDialog(this, "Documento gerado com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Erro ao gerar documento:\n" + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
