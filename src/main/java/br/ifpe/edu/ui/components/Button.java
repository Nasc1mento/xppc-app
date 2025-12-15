package br.ifpe.edu.ui.components;

import javax.swing.*;

public class Button extends JButton {

    public Button icon(Icon defaultIcon) {
        super.setIcon(defaultIcon);
        return this;
    }
}
