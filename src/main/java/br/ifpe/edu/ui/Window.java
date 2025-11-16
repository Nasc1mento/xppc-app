package br.ifpe.edu.ui;


import br.ifpe.edu.ui.common.Page;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Window extends JFrame {
    private int currentPage = 0;

    private final CardLayout cardLayout = new CardLayout();

    private final JPanel cards = new JPanel(cardLayout);
    private final JPanel titleBar =  new JPanel(new BorderLayout());
    private final JPanel buttonsPanel = new JPanel();

    private final List<Page> forms = PagesList.getList();

    private final JButton backwardButton = new JButton("Anterior");
    private final JButton forwardButton = new JButton("Proximo");
    private final JButton closeButton = new JButton("x");

    private final List<String> titles = forms.stream()
            .map(Page::getTitle)
            .toList();

    private final Class<?>[] themes = new Class[] {
            FlatDarkLaf.class,
            FlatLightLaf.class,
            FlatDarculaLaf.class,
            FlatMacDarkLaf.class,
            FlatMacLightLaf.class
    };

    private final JComboBox<String> themeComboBox = new JComboBox<>(
            Arrays.stream(themes)
                    .map(Class::getSimpleName)
                    .map(s -> s.replace("Flat", "").replace("Laf"," "))
                    .toArray(String[]::new)
    );


    private final JLabel titleLabel;

    public Window() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1024, 768));
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        titleBar.setBackground(new Color(50, 50, 50));
        titleBar.setPreferredSize(new Dimension(0, 40));

        titleLabel = new JLabel("XPPC App");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        titleBar.add(titleLabel, BorderLayout.CENTER);

        this.themeComboBox.setPreferredSize(new Dimension(140, 30));
        themeComboBox.addActionListener(_ -> {
            int selected = themeComboBox.getSelectedIndex();
            try {
                var constructor = themes[selected].getDeclaredConstructor();
                UIManager.setLookAndFeel((LookAndFeel) constructor.newInstance());
                SwingUtilities.updateComponentTreeUI(this);
            } catch (Exception ignore) { }
        });

        titleBar.add(this.themeComboBox, BorderLayout.WEST);

        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(Color.RED);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.setPreferredSize(new Dimension(45, 30));
        closeButton.addActionListener(e -> System.exit(0));
        titleBar.add(closeButton, BorderLayout.EAST);

        this.add(titleBar, BorderLayout.NORTH);

        for (int i = 0; i < forms.size(); i++) {
            var p = forms.get(i);

            var wrapper = new JPanel(new BorderLayout());
            wrapper.setBorder(new EmptyBorder(20, 20, 20, 20));
            wrapper.add(p, BorderLayout.CENTER);

            cards.add(wrapper, String.valueOf(i));
        }
        add(cards, BorderLayout.CENTER);
        this.updateButtons();

        backwardButton.addActionListener(_ -> {
            if (currentPage > 0) {
                currentPage--;
                cardLayout.show(cards, String.valueOf(currentPage));
                titleLabel.setText(titles.get(currentPage));
            }
            this.updateButtons();
        });

        forwardButton.addActionListener(_ -> {
            if (currentPage < forms.size() - 1) {
                currentPage++;
                cardLayout.show(cards, String.valueOf(currentPage));
                titleLabel.setText(titles.get(currentPage));
            }

            this.updateButtons();
        });

        buttonsPanel.add(backwardButton);
        buttonsPanel.add(forwardButton);

        this.add(buttonsPanel, BorderLayout.SOUTH);

        titleLabel.setText(titles.get(currentPage));

        setVisible(true);
    }

    private void updateButtons() {
        this.backwardButton.setEnabled(this.currentPage > 0);
        this.forwardButton.setEnabled(this.currentPage < this.forms.size() - 1);
    }
}

