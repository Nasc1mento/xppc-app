package br.ifpe.edu.ui.common;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;


public class TextField extends JFormattedTextField {

    private String placeholder;

    public TextField() { }

    public TextField(final int pColumns) {
        setColumns(pColumns);
    }

    public TextField(final String pText) {
        super(pText);
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

    public void setPlaceholder(final String s) {
        this.placeholder = s;
    }

    public TextField setDouble() {

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');

        DecimalFormat dFormat = new DecimalFormat("#0.00", symbols);
        dFormat.setGroupingUsed(false);

        NumberFormatter formatter = new NumberFormatter(dFormat);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.0);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);

        this.setFormatterFactory(new DefaultFormatterFactory(formatter));
        return this;
    }

    public TextField setInteger() {
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);

        this.setFormatterFactory(new DefaultFormatterFactory(formatter));
        this.setText("0");
        return this;
    }
}