package br.ifpe.edu.launcher;

import br.ifpe.edu.config.AppConfig;
import br.ifpe.edu.ui.config.ThemeConfig;
import br.ifpe.edu.ui.frames.MainWindow;
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