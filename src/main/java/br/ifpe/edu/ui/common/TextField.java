package br.ifpe.edu.ui.common;

import java.awt.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.text.Document;


public class TextField extends JTextField {

    private String placeholder;

    public TextField() { }

    public TextField(
            final Document pDoc,
            final String pText,
            final int pColumns)
    {
        super(pDoc, pText, pColumns);
    }

    public TextField(final int pColumns) {
        super(pColumns);
    }

    public TextField(final String pText) {
        super(pText);
    }

    public TextField(final String pText, final int pColumns) {
        super(pText, pColumns);
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

    public String getPlaceholder() {
        return this.placeholder;
    }

}