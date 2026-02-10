package br.edu.ifpe.config;

import br.edu.ifpe.core.DocumentManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class AppConfig {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DocumentManager.loadResourceStream("application.properties")) {
            properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
        } catch (IOException _) {

        }
    }

    public static String getVersion() {
        return properties.getProperty(getKey("version"), "Versão Desconhecida");
    }

    public static String getName() {
        return properties.getProperty(getKey("name"), "Nome Desconhecido");
    }

    private static String getKey(String name) {
        return "app." + name;
    }
}
