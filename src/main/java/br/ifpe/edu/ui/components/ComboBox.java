package br.ifpe.edu.ui.components;

import br.ifpe.edu.models.enums.ILabeledEnum;
import br.ifpe.edu.ui.renderers.LabeledEnumRenderer;

import javax.swing.*;

public class ComboBox<T> extends JComboBox<T> implements IComponent {

    { setRenderer(new LabeledEnumRenderer()); }

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

        var selectedItem = getSelectedItem();

        if (selectedItem == null) {
           return null;
        }

        if (selectedItem instanceof ILabeledEnum<?> s) {
            return s.getLabel().toString();
        }

        return selectedItem.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getSelectedItem() {
        return (T) super.getSelectedItem();
    }
}
