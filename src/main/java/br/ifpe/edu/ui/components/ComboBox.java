package br.ifpe.edu.ui.components;

import javax.swing.*;

public class ComboBox<T> extends JComboBox<T> implements IComponent {


    public ComboBox(Iterable<? extends T> items) {
        this();
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

    @Override
    public String getStringValue() {
        T selectedItem = getSelectedItem();
        return selectedItem == null ? null : selectedItem.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getSelectedItem() {
        return (T) super.getSelectedItem();
    }
}
