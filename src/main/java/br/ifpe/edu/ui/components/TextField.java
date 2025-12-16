package br.ifpe.edu.ui.components;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;


public class TextField extends JFormattedTextField implements IComponent {

    private String placeholder;

    public TextField(final int pColumns) {
        setColumns(pColumns);
    }

    public TextField setPlaceholder(String pText) {
        placeholder = pText;
        return this;
    }

    @Override
    protected void paintComponent(final Graphics pG) {
        super.paintComponent(pG);

        if (placeholder == null || placeholder.isEmpty() || !getText().isEmpty()) {
            return;
        }

        final var g = (Graphics2D) pG;
        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g.setColor(getDisabledTextColor());
        g.drawString(placeholder, getInsets().left, pG.getFontMetrics() .getMaxAscent() + getInsets().top);
    }

    public TextField setDouble() {

        var symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');

        var dFormat = new DecimalFormat("#0.00", symbols);
        dFormat.setGroupingUsed(false);

        var formatter = new NumberFormatter(dFormat);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.0);
        formatter.setAllowsInvalid(true);
        formatter.setCommitsOnValidEdit(true);

        this.setFormatterFactory(new DefaultFormatterFactory(formatter));
        return this;
    }

    public TextField setInteger() {
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);

        var formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setAllowsInvalid(true);
        formatter.setCommitsOnValidEdit(true);

        this.setFormatterFactory(new DefaultFormatterFactory(formatter));
        return this;
    }

    @Override
    public String getStringValue() {
        return getText();
    }

    public void clear() {
        setText("");
    }
}