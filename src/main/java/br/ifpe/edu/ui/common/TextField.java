package br.ifpe.edu.ui.common;

import java.awt.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.*;
import javax.swing.text.*;


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

    public TextField setNumeric() {
        PlainDocument pd = (PlainDocument) this.getDocument();
        pd.setDocumentFilter(new DocumentFilter() {

            final String regexp = "^[0-9]+(\\\\.[0-9]+)?$";

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches(regexp)) super.insertString(fb, offset, string, attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches(regexp)) super.replace(fb, offset, length, text, attrs);
            }
        });

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isBlank()) setText("0");
            }
        });

        this.setText("0");

        return this;
    }

}