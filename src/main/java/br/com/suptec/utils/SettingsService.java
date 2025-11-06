package br.com.suptec.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Serviço simples para persistência de preferências/ configurações do usuário.
 *
 * Implementação leve que grava um arquivo Properties no diretório do usuário
 * (user.home) com nome ".suptech_settings.properties". É suficiente para salvar
 * preferências como tema, idioma e outras flags pequenas.
 *
 * Observações:
 * - Não utiliza banco de dados nem criptografia (não armazenar segredos aqui)
 * - Carrega/salva o arquivo toda vez que um valor é setado (simplicidade sobre
 *   desempenho). Para cenários com muitas gravações, considerar cache em memória
 *   e flush periódico.
 */
public class SettingsService {

    private static final String FILENAME = ".suptech_settings.properties";

    /**
     * Retorna o File que representa o arquivo de configurações no diretório do
     * usuário. Não cria o arquivo automaticamente.
     */
    private static File getSettingsFile() {
        String userHome = System.getProperty("user.home");
        return new File(userHome, FILENAME);
    }

    /**
     * Carrega as propriedades do arquivo de configurações. Se o arquivo não
     * existir, retorna um Properties vazio.
     */
    public static Properties load() {
        Properties p = new Properties();
        File f = getSettingsFile();
        if (f.exists()) {
            try (FileInputStream in = new FileInputStream(f)) {
                p.load(in);
            } catch (IOException e) {
                // Falha ao ler: log para depuração, mas retornamos as propriedades
                // vazias para que a aplicação continue funcionando.
                System.err.println("Erro ao ler settings: " + e.getMessage());
            }
        }
        return p;
    }

    /**
     * Persiste as propriedades no arquivo de configurações. Substitui o conteúdo
     * anterior do arquivo.
     */
    public static void save(Properties props) {
        File f = getSettingsFile();
        try (FileOutputStream out = new FileOutputStream(f)) {
            props.store(out, "SUPTECH Settings");
        } catch (IOException e) {
            // Log de erro, mas não propaga exceção para não quebrar a UI
            System.err.println("Erro ao salvar settings: " + e.getMessage());
        }
    }

    /**
     * Lê um valor da configuração com fallback para defaultValue se não existir.
     */
    public static String get(String key, String defaultValue) {
        Properties p = load();
        return p.getProperty(key, defaultValue);
    }

    /**
     * Grava um único par chave/valor. Carrega as propriedades atuais, atualiza a
     * chave e salva o arquivo (comportamento simples e seguro para poucas
     * chaves).
     */
    public static void set(String key, String value) {
        Properties p = load();
        p.setProperty(key, value);
        save(p);
    }
}
