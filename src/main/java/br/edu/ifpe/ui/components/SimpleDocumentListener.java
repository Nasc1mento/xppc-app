package br.edu.ifpe.ui.components;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public record SimpleDocumentListener(Runnable action) implements DocumentListener {
    public void insertUpdate(DocumentEvent e) {
        action.run();
    }

    public void removeUpdate(DocumentEvent e) {
        action.run();
    }

    public void changedUpdate(DocumentEvent e) {
        action.run();
    }
}
