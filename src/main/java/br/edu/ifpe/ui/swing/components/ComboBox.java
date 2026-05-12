package br.edu.ifpe.ui.swing.components;

import javax.swing.*;
import java.awt.event.ItemEvent;


public class ComboBox<T> extends JComboBox<T> implements IInput {

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

    @Override
    public void addChangeListener(Runnable r) {
        addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED || e.getStateChange() == ItemEvent.DESELECTED) {
                r.run();
            }
        });
    }

    @Override
    public void clear() {
        if (getItemCount() > 0) {
            setSelectedIndex(0);
        }
    }
}
