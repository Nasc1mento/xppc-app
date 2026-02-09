package br.edu.ifpe.ui.renderers;

import br.edu.ifpe.enums.ILabeledEnum;

import javax.swing.*;
import java.awt.*;

public class LabeledEnumRenderer<T extends Enum<T> & ILabeledEnum> extends DefaultListCellRenderer {

    @SuppressWarnings("unchecked")
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        setText(((T)value).getLabel());

        return this;
    }
}
