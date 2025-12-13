package br.ifpe.edu.ui;

import br.ifpe.edu.ui.common.ComboBox;
import br.ifpe.edu.ui.common.Page;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class Window extends JFrame {
    private int currentPage = 0;

    private final CardLayout cardLayout = new CardLayout();

    private final JPanel cards = new JPanel(cardLayout);
    private final JPanel titleBar = new JPanel(new BorderLayout());

    private final JPanel buttonsPanel = new JPanel(new BorderLayout());
    private final JPanel pageNumbersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

    private final List<Page> forms = PagesList.getList();

    private final JButton backwardButton = new JButton("<< Anterior");
    private final JButton forwardButton = new JButton("Próximo >>");

    private final List<String> titles = forms.stream()
            .map(Page::getTitle)
            .toList();

    private final List<Class<?>> themes = List.of(
            FlatDarkLaf.class,
            FlatLightLaf.class,
            FlatDarculaLaf.class,
            FlatMacDarkLaf.class,
            FlatMacLightLaf.class
    );

    private final ComboBox<String> themeComboBox = new ComboBox<>(
            themes.stream()
                    .map(Class::getSimpleName)
                    .map(s -> s.replace("Flat", "").replace("Laf", " "))
                    .toList()
    );

    private final JLabel titleLabel;

    public Window() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1024, 720));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        titleBar.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, UIManager.getColor("Component.borderColor")),
                new EmptyBorder(15, 20, 15, 20)
        ));

        titleLabel = new JLabel("XPPC App");
        titleLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        titleBar.add(titleLabel, BorderLayout.CENTER);

        themeComboBox.setPreferredSize(new Dimension(140, 30));
        titleBar.add(themeComboBox, BorderLayout.WEST);
        add(titleBar, BorderLayout.NORTH);

        for (int i = 0; i < forms.size(); i++) {
            var p = forms.get(i);
            var wrapper = new JPanel(new BorderLayout());
            wrapper.setBorder(new EmptyBorder(20, 20, 20, 20));
            wrapper.add(p, BorderLayout.CENTER);
            cards.add(wrapper, String.valueOf(i));
        }
        add(cards, BorderLayout.CENTER);

        buttonsPanel.setBorder(new CompoundBorder(
                new MatteBorder(1, 0, 0, 0, UIManager.getColor("Component.borderColor")),
                new EmptyBorder(15, 20, 15, 20)
        ));
        buttonsPanel.add(pageNumbersPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        setupListeners();
        navigateTo(0);
        setupIcon();
        setVisible(true);
    }

    private void navigateTo(int pageIndex) {
        if (pageIndex < 0 || pageIndex >= forms.size()) return;

        this.currentPage = pageIndex;

        cardLayout.show(cards, String.valueOf(currentPage));

        titleLabel.setText(titles.get(currentPage));

        backwardButton.setEnabled(currentPage > 0);
        forwardButton.setEnabled(currentPage < forms.size() - 1);

        updatePageNumbers();
        cards.requestFocusInWindow();
    }

    private void updatePageNumbers() {
        pageNumbersPanel.removeAll();

        pageNumbersPanel.add(backwardButton, BorderLayout.WEST);
        for (int i = 0; i < forms.size(); i++) {
            int pageIndex = i;
            var btn = new JButton(String.valueOf(i + 1));
            btn.putClientProperty("JButton.buttonType", "roundRect");
            btn.setMargin(new Insets(2, 8, 2, 8));

            if (i == currentPage) {
                btn.setEnabled(false);
                btn.setFont(btn.getFont().deriveFont(Font.BOLD));
            } else {
                btn.addActionListener(_ -> navigateTo(pageIndex));
            }

            pageNumbersPanel.add(btn);
        }

        pageNumbersPanel.add(forwardButton, BorderLayout.EAST);

        pageNumbersPanel.revalidate();
        pageNumbersPanel.repaint();
    }

    private void setupListeners() {
        themeComboBox.addActionListener(_ -> {
            int selected = themeComboBox.getSelectedIndex();
            try {
                var constructor = themes.get(selected).getDeclaredConstructor();
                UIManager.setLookAndFeel((LookAndFeel) constructor.newInstance());
                SwingUtilities.updateComponentTreeUI(this);
                updatePageNumbers();
            } catch (Exception _) { }
        });

        forwardButton.addActionListener(_ -> navigateTo(currentPage + 1));
        backwardButton.addActionListener(_ -> navigateTo(currentPage - 1));
    }

    private void setupIcon() {
        URL iconURL = Thread.currentThread().getContextClassLoader().getResource("ifpe.png");

        if (iconURL != null) {
            var icon = new ImageIcon(iconURL);
            setIconImage(icon.getImage());
        }
    }
}