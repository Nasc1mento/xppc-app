package br.edu.ifpe.ui.swing.pages;

import br.edu.ifpe.ui.swing.components.Page;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Cover extends Page {

    public Cover() {
        setOpaque();
        this.add(new XPPCLogo());
    }

    @Override
    public String getTitle() {
        return "Bem-vindo(a)";
    }

    public static class XPPCLogo extends JLabel {

        private int clickCounter = 0;

        public XPPCLogo() {
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);

            setCursor(new Cursor(Cursor.HAND_CURSOR));

            updateIconState();

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    clickCounter++;
                    if (clickCounter > 12) clickCounter = 0;

                    updateIconState();
                }
            });
        }

        private enum LogoState {
            INITIAL("images/xppc_full.svg", 0.5f),
            XPPC_SMALL("images/xppc_icon.svg", 1f),
            IFPE_SMALL("images/ifpe_icon.svg", 1f),
            IFPE_FULL("images/ifpe_full.svg", 0.5f);

            final String path;
            final float scale;

            LogoState(String path, float scale) {
                this.path = path;
                this.scale = scale;
            }
        }

        private void updateIconState() {
            if (clickCounter >= 12) clickCounter = 0;

            LogoState state = switch (clickCounter) {
                case 3, 4, 5 -> LogoState.XPPC_SMALL;
                case 6, 7, 8 -> LogoState.IFPE_SMALL;
                case 9, 10, 11 -> LogoState.IFPE_FULL;
                default -> LogoState.INITIAL;
            };

            this.setIcon(new FlatSVGIcon(state.path, state.scale));
        }
    }
}