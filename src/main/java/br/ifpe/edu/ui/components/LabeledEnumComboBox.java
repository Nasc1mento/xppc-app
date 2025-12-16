package br.ifpe.edu.ui.components;

import br.ifpe.edu.models.enums.ILabeledEnum;
import br.ifpe.edu.ui.renderers.LabeledEnumRenderer;

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
