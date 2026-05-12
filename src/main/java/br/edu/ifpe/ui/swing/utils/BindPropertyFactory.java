package br.edu.ifpe.ui.swing.utils;

import br.edu.ifpe.ui.swing.components.IInput;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class BindPropertyFactory {

    private BindPropertyFactory() {  }

    public static BindProperty create() {
        return new BindProperty();
    }

    public static class BindProperty {
        private final List<BooleanSupplier> validators = new ArrayList<>();
        private Consumer<Boolean> onStatusChange;

        public BindProperty bind(IInput c) {
            validators.add(() -> isNotNullAndNotEmpty(c));
            c.addChangeListener(this::check);
            return this;
        }

        private boolean isNotNullAndNotEmpty(IInput c) {
            return c.getStringValue() != null && !c.getStringValue().trim().isEmpty();
        }

        public void onChange(Consumer<Boolean> action) {
            this.onStatusChange = action;
            check();
        }

        private void check() {
            if (onStatusChange == null) return;

            boolean isValid = validators.stream().allMatch(BooleanSupplier::getAsBoolean);
            onStatusChange.accept(isValid);
        }
    }
}