package br.edu.ifpe.ui.config;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ThemeConfig {

    public static void setup() {
        System.setProperty("flatlaf.useWindowDecorations", "true");
        setupUIManager();

        try {
            FlatDarkLaf.setup();
        } catch (Exception _) {  }
    }

    private static void setupUIManager() {
        var fontBold = new Font("OpenSans", Font.BOLD, 13);
        var fontRegular = new Font("OpenSans", Font.PLAIN, 13);
        var accentColor = new Color(40, 90, 160);

        UIManager.put("Component.arc", 12);
        UIManager.put("Button.arc", 20);
        UIManager.put("TextComponent.arc", 12);
        UIManager.put("CheckBox.arc", 6);

        UIManager.put("Label.font", fontBold);

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
        UIManager.put("TableHeader.font", fontBold);

        UIManager.put("Table.showVerticalLines", true);
        UIManager.put("Table.showHorizontalLines", true);

        UIManager.put("Table.selectionBackground", accentColor);
        UIManager.put("Table.selectionForeground", Color.WHITE);

        UIManager.put("OptionPane.minimumSize", new Dimension(400, 150));
        UIManager.put("OptionPane.border", new EmptyBorder(20, 20, 15, 20));
        UIManager.put("OptionPane.messageAreaBorder", new EmptyBorder(10, 15, 10, 15));
        UIManager.put("OptionPane.buttonPadding", 12);
        UIManager.put("OptionPane.messageFont", fontRegular);

        UIManager.put("ProgressBar.foreground", accentColor);
        UIManager.put("ProgressBar.background", new Color(0, 0, 0, 0));
        UIManager.put("ProgressBar.font", fontBold);
        UIManager.put("ProgressBar.arc", 999);
        UIManager.put("ProgressBar.height", 14);
    }
}