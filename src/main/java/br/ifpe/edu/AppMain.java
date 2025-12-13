package br.ifpe.edu;

import br.ifpe.edu.ui.Window;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AppMain {

    static void main() {
        System.setProperty("sun.java2d.uiScale", "1");
        System.setProperty("flatlaf.useWindowDecorations", "true");

        try {
            var sansSerifBold = new Font("SansSerif", Font.BOLD, 13);
            var sansSerifRegular = new Font("SansSerif", Font.PLAIN, 13);

            UIManager.put("Component.arc", 12);
            UIManager.put("Button.arc", 20);
            UIManager.put("TextComponent.arc", 12);
            UIManager.put("CheckBox.arc", 6);

            UIManager.put("Label.font", sansSerifBold);

            UIManager.put("Component.arrowType", "chevron");
            UIManager.put("Component.focusWidth", 1);
            UIManager.put("Component.innerFocusWidth", 0);

            UIManager.put("ScrollBar.width", 12);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.trackArc", 999);

            UIManager.put("TabbedPane.showTabSeparators", true);
            UIManager.put("TabbedPane.selectedBackground", new Color(50, 50, 50));

            UIManager.put("Table.rowHeight", 30);
            UIManager.put("TableHeader.height", 35);
            UIManager.put("TableHeader.font", sansSerifBold);

            UIManager.put("Table.showVerticalLines", true);
            UIManager.put("Table.showHorizontalLines", true);

            UIManager.put("Table.selectionBackground", new Color(40, 90, 160));
            UIManager.put("Table.selectionForeground", Color.WHITE);

            UIManager.put("OptionPane.minimumSize", new Dimension(400, 150));
            UIManager.put("OptionPane.border", new EmptyBorder(20, 20, 15, 20));
            UIManager.put("OptionPane.messageAreaBorder", new EmptyBorder(10, 15, 10, 15));
            UIManager.put("OptionPane.buttonPadding", 12);
            UIManager.put("OptionPane.messageFont", sansSerifRegular);

            UIManager.setLookAndFeel(new FlatDarkLaf());

        } catch (Exception _) {

        }

        SwingUtilities.invokeLater(Window::new);
    }
}