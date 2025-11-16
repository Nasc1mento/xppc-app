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

    protected void addRow(final Component c1, final Component c2) {
        gbc.gridx = 0;
        gbc.gridy = this.row;
        gbc.anchor = GridBagConstraints.EAST;
        this.add(c1, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        this.add(c2, gbc);

        this.row++;
    }

    public abstract String getTitle();

    public abstract void onSubmit();
}
