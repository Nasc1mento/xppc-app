package br.edu.ifpe.ui.swing.renderers;

import br.edu.ifpe.core.domain.enums.ILabeledEnum;

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
