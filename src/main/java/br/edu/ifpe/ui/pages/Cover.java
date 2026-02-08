package br.edu.ifpe.ui.pages;

import br.edu.ifpe.config.AppConfig;
import br.edu.ifpe.ui.components.Page;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;


@Slf4j
public class Cover extends Page {
    private final JLabel titleLabel = new JLabel(AppConfig.getShortname());
    private final JButton aboutButton = new JButton();

    private FlatSVGIcon backgroundImage;

    public Cover() {
        setOpaque();
        setupBackgroundImage();
        setupLayout();
        setupLabels();
    }

    private void setupLabels() {
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
    }


    private void setupLayout() {
        aboutButton.setContentAreaFilled(false);
        aboutButton.setFocusPainted(false);

        aboutButton.setIcon(UIManager.getIcon("OptionPane.questionIcon"));

    }

    // if you need to set background image in other pages, I recommend move this code to parent class
    private void setupBackgroundImage() {
        try {
            FlatSVGIcon.ColorFilter filter = new FlatSVGIcon.ColorFilter( color -> {
                if (color.equals(Color.BLACK)) {
                    return UIManager.getColor("Label.foreground");
                }
                return color;
            });

            backgroundImage = new FlatSVGIcon("xppc.svg");
            backgroundImage.setColorFilter(filter);

        } catch (Exception e) {
            log.info("Failed to load SVG: {}", e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            int targetWidth;
            targetWidth = 900;

            float scale = (float) targetWidth / backgroundImage.getIconWidth();
            int targetHeight = Math.round(backgroundImage.getIconHeight() * scale);

            int x = (getWidth() - targetWidth) / 2;
            int y = (getHeight() - targetHeight) / 2;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

            g2d.translate(x, y);
            g2d.scale(scale, scale);
            backgroundImage.paintIcon(this, g2d, 0, 0);

            g2d.dispose();
        }
    }

    @Override
    public String getTitle() {
        return "Bem-vindo(a)";
    }
}
