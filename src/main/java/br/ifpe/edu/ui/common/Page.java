package br.ifpe.edu.ui.common;

import javax.swing.*;
import java.awt.*;

public abstract class Page extends JPanel {

    protected final GridBagConstraints gbc;

    private final JPanel contentPanel;
    private int row = 0;

    public Page() {
        super(new BorderLayout());

        contentPanel = new JPanel(new GridBagLayout());

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        var scrollPane = new JScrollPane(contentPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    protected void addTable(final JTable table) {
        var sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(0, 200));

        gbc.gridx = 0;
        gbc.gridy = this.row;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        contentPanel.add(sp, gbc);

        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        row++;
    }

    protected void addRow(final Component ...cs) {
        for (int i = 0; i < cs.length; i++) {
            if (i == 0) {
                gbc.gridx = 0;
                gbc.gridy = this.row;
                gbc.anchor = GridBagConstraints.EAST;
                contentPanel.add(cs[0], gbc);
            } else {
                gbc.gridx = 1;
                gbc.anchor = GridBagConstraints.WEST;
                contentPanel.add(cs[i], gbc);
                row++;
            }
        }
        row++;
    }

    protected void addRow(final Component cs, final int alignment) {
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.anchor = alignment;
        contentPanel.add(cs, gbc);
        row++;
    }

    public abstract String getTitle();
}