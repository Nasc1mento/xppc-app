package br.edu.ifpe.ui.pages;

import br.edu.ifpe.config.AppConfig;
import br.edu.ifpe.readers.PeopleReader;
import br.edu.ifpe.ui.components.Page;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;


@Slf4j
public class Cover extends Page {
    private final JLabel titleLabel = new JLabel(AppConfig.getName());
    private final JButton aboutButton = new JButton();
    private final PeopleReader peopleReader = PeopleReader.INSTANCE;

    private Image backgroundImage;

    public Cover() {
        setOpaque();
        setupBackgroundImage();
        setupLayout();
        setupLabels();
        setupListener();
    }

    private void setupListener() {
        aboutButton.addActionListener(_ -> {
            var sb = new StringBuilder()
                    .append("<html>")
                    .append("<p><b>Versão:</b> ").append(AppConfig.getVersion()).append("</p>")
                    .append("<hr>")
                    .append("<p>").append(AppConfig.getName())
                    .append("</p>")
                    .append("<br>")
                    .append("<p><b>Equipe</b></p>")
                    .append("<hr>")
                    .append("<ul>");

            for (var person : peopleReader.get()) {
                sb.append("<li><b>")
                        .append(person[PeopleReader.Columns.NAME.getIndex()])
                        .append("</b>: ")
                        .append(person[PeopleReader.Columns.ROLE.getIndex()])
                        .append("</li>");
            }

            sb.append("</ul></html>");


            JOptionPane.showMessageDialog(
                    this,
                    sb.toString(),
                    "Sobre",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }

    private void setupLabels() {
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24f));
    }


    private void setupLayout() {
        aboutButton.setContentAreaFilled(false);
        aboutButton.setFocusPainted(false);

        aboutButton.setIcon(UIManager.getIcon("OptionPane.questionIcon"));

        addRow(titleLabel, aboutButton);
    }

    // caso precise usar diversas imagens de fundo diferentes em lugares diferente, recomendo mover para a classe pai
    private void setupBackgroundImage() {
        String resourceName = "/if_transparente.png";

        try {
            URL resource = getClass().getResource(resourceName);
            if (resource != null) {
                this.backgroundImage = ImageIO.read(resource);
            }
        } catch (IOException e) {
            log.info("Failed to load background image: {}", resourceName);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = 250;
            int height = (width * backgroundImage.getHeight(null)) / backgroundImage.getWidth(null);

            int x = 10;
            int y = getHeight() - height - x;

            float opacity = 0.3f;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

            g2d.drawImage(backgroundImage, x, y, width, height, this);

            g2d.dispose();
        }
    }

    @Override
    public String getTitle() {
        return "Bem-vindo(a)";
    }
}
