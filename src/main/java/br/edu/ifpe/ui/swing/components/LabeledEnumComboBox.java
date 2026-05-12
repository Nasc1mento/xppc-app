package br.edu.ifpe.ui.swing.components;

import br.edu.ifpe.core.domain.enums.ILabeledEnum;
import br.edu.ifpe.ui.swing.renderers.LabeledEnumRenderer;

public class LabeledEnumComboBox<T extends Enum<T> & ILabeledEnum> extends ComboBox<T> {

    public LabeledEnumComboBox(Class<T> cls) {
        super(cls.getEnumConstants());
        setRenderer(new LabeledEnumRenderer<T>());
    }

    @Override
    public String getStringValue() {
        T selected = getSelectedItem();
        return selected == null ? null : selected.getLabel();
    }
}
