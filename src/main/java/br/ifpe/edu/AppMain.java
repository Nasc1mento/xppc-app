package br.ifpe.edu;

import br.ifpe.edu.ui.Window;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class AppMain {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
           e.printStackTrace();
        }

        SwingUtilities.invokeLater(Window::new);
    }
}
