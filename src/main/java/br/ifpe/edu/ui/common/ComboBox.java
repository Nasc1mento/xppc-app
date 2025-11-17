package br.ifpe.edu.ui.common;

import javax.swing.*;

public class ComboBox<T> extends JComboBox<T> {

    public ComboBox(Iterable<? extends T> items) {
        addAll(items);
    }

    public ComboBox(T[] items) {
        super(items);
    }

    public ComboBox() {
        super();
    }

    public final void addAll(Iterable<? extends T> items) {
        for (T item : items) {
            addItem(item);
        }
    }
}
