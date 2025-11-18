package br.ifpe.edu.ui.common;

import javax.swing.*;
import java.awt.*;

public abstract class Page extends JPanel {

    protected final GridBagConstraints gbc;

    private int row = 0;

    public Page() {
        this.setLayout(new GridBagLayout());
        this.gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
    }

    protected void addTable(JTable table) {
        var sp = new JScrollPane(table);
        gbc.gridx = 0;
        gbc.gridy = this.row;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(sp, gbc);
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        this.row++;
    }


    protected void addRow(final Component ...cs) {
        for (int i = 0; i < cs.length; i++) {
            if (i == 0) {
                gbc.gridx = 0;
                gbc.gridy = this.row;
                gbc.anchor = GridBagConstraints.EAST;
                this.add(cs[0], gbc);
            } else {
                gbc.gridx = 1;
                gbc.anchor = GridBagConstraints.WEST;
                this.add(cs[i], gbc);
                this.row++;
            }
        }
    }


    public abstract String getTitle();

    public abstract void onSubmit();
}
