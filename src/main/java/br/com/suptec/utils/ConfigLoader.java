package br.com.suptec.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static ConfigLoader instance;
    private Properties properties;
    
    private ConfigLoader() {
        properties = new Properties();
        loadProperties();
    }
    
    public static ConfigLoader getInstance() {
        if (instance == null) {
            instance = new ConfigLoader();
        }
        return instance;
    }
    
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.err.println("Arquivo application.properties não encontrado!");
                // Valores padrão
                properties.setProperty("api.base.url", "http://localhost:5165/api/");
                properties.setProperty("api.timeout", "30");
                return;
            }
            properties.load(input);
            System.out.println("Configurações carregadas com sucesso!");
            System.out.println("API URL: " + getApiBaseUrl());
        } catch (IOException ex) {
            System.err.println("Erro ao carregar configurações: " + ex.getMessage());
            ex.printStackTrace();
            // Valores padrão em caso de erro
            properties.setProperty("api.base.url", "http://localhost:5165/api/");
            properties.setProperty("api.timeout", "30");
        }
    }
    
    public String getApiBaseUrl() {
        return properties.getProperty("api.base.url", "http://localhost:5165/api/");
    }
    
    public int getApiTimeout() {
        return Integer.parseInt(properties.getProperty("api.timeout", "30"));
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
