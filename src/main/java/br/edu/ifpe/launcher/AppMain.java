package br.edu.ifpe.launcher;

import br.edu.ifpe.config.AppConfig;
import br.edu.ifpe.ui.config.ThemeConfig;
import br.edu.ifpe.ui.frames.MainWindow;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class AppMain {

    static void main() {

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

        log.info("App version: {}", AppConfig.getVersion());
        log.info("OS: {}, ({})", System.getProperty("os.name"), System.getProperty("os.version"));
        log.info("Resolution: {}", (int) size.getWidth() + "x" + (int) size.getHeight());

        ThemeConfig.setup();

        SwingUtilities.invokeLater(MainWindow::new);
    }
}