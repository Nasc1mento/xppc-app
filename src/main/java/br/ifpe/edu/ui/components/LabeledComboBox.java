package br.ifpe.edu.ui.components;

import br.ifpe.edu.models.enums.ILabeledEnum;
import br.ifpe.edu.ui.renderers.LabeledEnumRenderer;

import javax.swing.*;

public class LabeledComboBox<T extends Enum<T> & ILabeledEnum<T>> extends ComboBox<T> {

    { setRenderer(new LabeledEnumRenderer()); }

    public LabeledComboBox(Class<T> enumCls) {
        super(enumCls.getEnumConstants());
    }


    @Override
    public String getStringValue() {
        T selected = getSelectedItem();
        return selected == null ? null : String.valueOf(selected);
    }
}
