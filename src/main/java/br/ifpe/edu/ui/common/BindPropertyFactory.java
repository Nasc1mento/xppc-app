package br.ifpe.edu.ui.common;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class BindPropertyFactory {

    private BindPropertyFactory() {

    }

    public static BindProperty create() {
        return new BindProperty();
    }

    public static class BindProperty {
        private final List<BooleanSupplier> validators = new ArrayList<>();
        private Consumer<Boolean> onStatusChange;

        public BindProperty bind(IComponent c) {
            if (c instanceof TextField) {
                validators.add(() -> isEmptyAndNull(c));
                ((TextField)c).getDocument().addDocumentListener(new SimpleDocumentListener(this::check));
            } else if (c instanceof ComboBox<?>) {
                validators.add(() -> isEmptyAndNull(c));
                ((ComboBox<?>)c).addItemListener(e -> {
                    if (e.getStateChange() == ItemEvent.SELECTED || e.getStateChange() == ItemEvent.DESELECTED) {
                        check();
                    }
                });
            }

            return this;
        }

        private boolean isEmptyAndNull(IComponent c) {
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

    private record SimpleDocumentListener(Runnable action) implements DocumentListener {
        public void insertUpdate(DocumentEvent e) {
            action.run();
        }

        public void removeUpdate(DocumentEvent e) {
            action.run();
        }

        public void changedUpdate(DocumentEvent e) {
            action.run();
        }
    }
}