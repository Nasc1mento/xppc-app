package br.edu.ifpe.ui.components;

import javax.swing.*;

public interface IComponent {


    @SuppressWarnings("unchecked")
    default <T extends JComponent> T withName(String name) {
        T c = (T) this;
        c.setName(name);
        return c;
    }
}
