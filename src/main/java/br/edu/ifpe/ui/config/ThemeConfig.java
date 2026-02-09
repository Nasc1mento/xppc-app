package br.edu.ifpe.ui.config;

import br.edu.ifpe.services.DocumentManager;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@Slf4j
public class ThemeConfig {

    public static void setup() {
        System.setProperty("sun.java2d.uiScale", "1.0");
        System.setProperty("flatlaf.useWindowDecorations", "true");
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        setupFonts();
        setupFlatLaF();
        setupUIManager();
    }

    private static void setupUIManager() {

        var fontRegular = new Font("Open Sans", Font.PLAIN, 13);
        var fontBold = new Font("Open Sans", Font.BOLD, 13);

        var accentColor = new Color(40, 90, 160);
        var transparent = new Color(0, 0, 0, 0);

        UIManager.put("defaultFont", fontRegular);

        UIManager.put("Component.arc", 12);
        UIManager.put("Button.arc", 20);
        UIManager.put("TextComponent.arc", 12);
        UIManager.put("CheckBox.arc", 6);

        UIManager.put("Label.font", fontBold);

        UIManager.put("TableHeader.font", fontBold);
        UIManager.put("Table.rowHeight", 30);
        UIManager.put("TableHeader.height", 35);
        UIManager.put("Table.showVerticalLines", true);
        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("Table.selectionBackground", accentColor);
        UIManager.put("Table.selectionForeground", Color.WHITE);

        UIManager.put("ScrollBar.width", 12);
        UIManager.put("ScrollBar.thumbArc", 999);

        UIManager.put("OptionPane.messageFont", fontRegular);
        UIManager.put("OptionPane.buttonFont", fontBold);

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
        UIManager.put("ProgressBar.background", transparent);
        UIManager.put("ProgressBar.font", fontBold);
        UIManager.put("ProgressBar.arc", 999);
        UIManager.put("ProgressBar.height", 14);
    }

    private static void setupFlatLaF() {
        try {
            FlatDarkLaf.setup();
        } catch (Exception e) {
            log.info("Failed to setup FlatLaF. Using default ugly interface instead");
        }

        FlatSVGIcon.ColorFilter.getInstance().setMapper(color -> {
            if (color.equals(Color.BLACK)) {
                return UIManager.getColor("Label.foreground");
            }
            return color;
        });
    }

    private static void setupFonts() {
        var fonts = new String[] {
                "fonts/open-sans.regular.ttf",
                "fonts/open-sans.bold.ttf"
        };

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        try {
            for (var font : fonts) {
                ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, DocumentManager.loadResourceStream(font)));
            }
        } catch (Exception e) {
            log.info("Failed to load resource fonts. Using default font instead");
        }
    }
}