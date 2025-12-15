package br.ifpe.edu.ui.pages;

import br.ifpe.edu.services.replacers.ReplacerList;
import br.ifpe.edu.helpers.DocumentHelper;
import br.ifpe.edu.ui.components.ISubmittable;
import br.ifpe.edu.ui.components.IValidatable;
import br.ifpe.edu.ui.components.Page;

import javax.swing.*;
import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

public class Generation extends Page implements ISubmittable {

    private final JButton generateButton = new JButton("Gerar Documento");
    private final JProgressBar progressBar = new JProgressBar();

    public Generation() {
        setupListeners();
        setupLayout();
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

    private void setupLayout() {
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        progressBar.setString("Só um momento...");
        progressBar.setStringPainted(true);

        addRow(generateButton);
        addRow(progressBar);
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
            DocumentHelper.INSTANCE.setOutputPath(Paths.get(Paths.get(selectedDir.toURI()).toString(), "ppc.docx"));

            generate();
        }
    }

    private void generate() {
        generateButton.setEnabled(false);
        progressBar.setVisible(true);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                try (var rl = new ReplacerList()) {
                    rl.cAll();
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();

                    JOptionPane.showMessageDialog(
                            Generation.this,
                            "Documento gerado com sucesso!"
                    );
                } catch (InterruptedException | ExecutionException ex) {
                    Throwable cause = ex.getCause();
                    JOptionPane.showMessageDialog(
                            Generation.this,
                            String.format("<html>Erro inesperado ao gerar documento<hr><br><b>Erro: </b> <i>%s</i></html>",
                                    cause != null ? cause.getMessage() : ex.getMessage()),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                } finally {
                    generateButton.setEnabled(true);
                    progressBar.setVisible(false);
                }
            }
        };

        worker.execute();
    }
}