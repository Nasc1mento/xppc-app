package br.edu.ifpe;

import br.edu.ifpe.config.AppConfig;
import br.edu.ifpe.ui.swing.config.UIConfig;
import br.edu.ifpe.ui.swing.frames.MainWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class AppMain {

    private static final Logger log = LoggerFactory.getLogger(AppMain.class);

    void main() {

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

        log.info("App version: {}", AppConfig.getVersion());
        log.info("OS: {}, ({})", System.getProperty("os.name"), System.getProperty("os.version"));
        log.info("Resolution: {}", (int) size.getWidth() + "x" + (int) size.getHeight());

        UIConfig.setup();

        SwingUtilities.invokeLater(MainWindow::new);
    }
}