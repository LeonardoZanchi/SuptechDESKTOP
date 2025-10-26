package br.com.suptec.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Utilitário para manipulação de JSON usando Gson
 */
public class JsonUtils {
    private static final Gson gson = new Gson();

    /**
     * Converte um objeto para JSON
     */
    public static String toJson(Object obj) {
        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            System.err.println("Erro ao converter para JSON: " + e.getMessage());
            return null;
        }
    }

    /**
     * Converte JSON para objeto
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            System.err.println("Erro ao converter JSON para objeto: " + e.getMessage());
            return null;
        }
    }
}
