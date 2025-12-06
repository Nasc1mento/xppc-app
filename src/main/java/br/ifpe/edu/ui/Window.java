package br.ifpe.edu.ui;


import br.ifpe.edu.ui.common.ComboBox;
import br.ifpe.edu.ui.common.Page;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
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
                    .map(s -> s.replace("Flat", "").replace("Laf"," "))
                    .toList()
    );


    private final JLabel titleLabel;

    public Window() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1440, 980));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        titleBar.setBackground(new Color(50, 50, 50));
        titleBar.setPreferredSize(new Dimension(0, 40));

        titleLabel = new JLabel("XPPC App");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
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
        updateButtons();

        buttonsPanel.add(backwardButton);
        buttonsPanel.add(forwardButton);

        add(buttonsPanel, BorderLayout.SOUTH);

        titleLabel.setText(titles.get(currentPage));

        setupListeners();

        setVisible(true);
    }

    private void setupListeners() {

        themeComboBox.addActionListener(_ -> {
            int selected = themeComboBox.getSelectedIndex();
            try {
                var constructor = themes.get(selected).getDeclaredConstructor();
                UIManager.setLookAndFeel((LookAndFeel) constructor.newInstance());
                SwingUtilities.updateComponentTreeUI(this);
            } catch (Exception _) { }
        });


        forwardButton.addActionListener(_ -> {
            if (currentPage < forms.size() - 1) {
                currentPage++;
                cardLayout.show(cards, String.valueOf(currentPage));
                titleLabel.setText(titles.get(currentPage));
            }

            this.updateButtons();
        });

        backwardButton.addActionListener(_ -> {
            if (currentPage > 0) {
                currentPage--;
                cardLayout.show(cards, String.valueOf(currentPage));
                titleLabel.setText(titles.get(currentPage));
            }
            this.updateButtons();
        });
    }

    private void updateButtons() {
        this.backwardButton.setEnabled(this.currentPage > 0);
        this.forwardButton.setEnabled(this.currentPage < this.forms.size() - 1);
    }
}

