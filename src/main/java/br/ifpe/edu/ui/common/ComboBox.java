package br.ifpe.edu.ui.common;

import javax.swing.*;
import java.util.Objects;

public class ComboBox<T> extends JComboBox<T> implements IComponent {

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

    public String getStringValue() {
        if (this.getSelectedItem() == null) {
           return null;
        }
        return this.getSelectedItem().toString();
    }
}
