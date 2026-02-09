package br.edu.ifpe.ui.pages;

import br.edu.ifpe.ui.components.Page;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Cover extends Page {

    public Cover() {
        setOpaque();
        this.add(new xPPCLogo());
    }

    @Override
    public String getTitle() {
        return "Bem-vindo(a)";
    }

    public static class xPPCLogo extends JPanel {
        private FlatSVGIcon icon;
        private int iconWidth;
        private int clickCounter = 0;

        public xPPCLogo() {
            initialIcon();

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    clickCounter++;

                    switch (clickCounter) {
                        case 3:
                            setIcon(new FlatSVGIcon("images/xppc_logo.svg"), 100);
                            break;
                        case 6:
                            setIcon(new FlatSVGIcon("images/ifpe.svg"), 100);
                            break;
                        case 9:
                            initialIcon();
                            clickCounter = 0;
                            break;
                    }
                }
            });
        }

        public void setIcon(FlatSVGIcon novoIcon, int width) {
            this.iconWidth = width;
            this.icon = novoIcon;
            repaint();
        }

        public void initialIcon() {
            setIcon(new FlatSVGIcon("images/xppc.svg"), 900);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (icon != null) {
                renderIcon(g);
            }
        }

        private void renderIcon(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

            float scale = (float) iconWidth / icon.getIconWidth();
            int targetHeight = Math.round(icon.getIconHeight() * scale);

            int x = (getWidth() - iconWidth) / 2;
            int y = (getHeight() - targetHeight) / 2;

            g2d.translate(x, y);
            g2d.scale(scale, scale);
            icon.paintIcon(this, g2d, 0, 0);

            g2d.dispose();
        }
    }
}
