package br.ifpe.edu;

import br.ifpe.edu.ui.Window;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class AppMain {

    static void main() {
        System.setProperty("sun.java2d.uiScale", "1");
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception _) { }

        SwingUtilities.invokeLater(Window::new);
    }
}
