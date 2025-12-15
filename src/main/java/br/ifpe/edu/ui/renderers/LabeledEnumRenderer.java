package br.ifpe.edu.ui.renderers;

import br.ifpe.edu.models.enums.ILabeledEnum;

import javax.swing.*;
import java.awt.*;

public class LabeledEnumRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof ILabeledEnum<?> le)
            setText(le.getLabel().toString());

        return this;
    }
}
