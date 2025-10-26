package br.com.suptec.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ApiService {
    private static final String BASE_URL = "http://localhost:5165/api/";
    private final HttpClient client;

    public ApiService() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Executa uma requisição POST para a API
     * @param endpoint Endpoint da API (sem a barra inicial)
     * @param jsonBody Corpo da requisição em JSON
     * @return Response com status code e body
     */
    public ApiResponse post(String endpoint, String jsonBody) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(30))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(response.statusCode(), response.body());
        } catch (Exception e) {
            System.err.println("Erro na requisição para " + endpoint + ": " + e.getMessage());
            return new ApiResponse(-1, null);
        }
    }

    /**
     * Executa uma requisição GET para a API
     * @param endpoint Endpoint da API (sem a barra inicial)
     * @return Response com status code e body
     */
    public ApiResponse get(String endpoint) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(30))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(response.statusCode(), response.body());
        } catch (Exception e) {
            System.err.println("Erro na requisição GET para " + endpoint + ": " + e.getMessage());
            return new ApiResponse(-1, null);
        }
    }

    /**
     * Executa uma requisição DELETE para a API
     * @param endpoint Endpoint da API com query params (ex: "Usuario/Excluir?usuarioId=xxx")
     * @return Response com status code e body
     */
    public ApiResponse delete(String endpoint) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(30))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(response.statusCode(), response.body());
        } catch (Exception e) {
            System.err.println("Erro na requisição DELETE para " + endpoint + ": " + e.getMessage());
            return new ApiResponse(-1, null);
        }
    }

    /**
     * Executa uma requisição PUT para a API
     * @param endpoint Endpoint da API (sem a barra inicial)
     * @param jsonBody Corpo da requisição em JSON
     * @return Response com status code e body
     */
    public ApiResponse put(String endpoint, String jsonBody) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(30))
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(response.statusCode(), response.body());
        } catch (Exception e) {
            System.err.println("Erro na requisição PUT para " + endpoint + ": " + e.getMessage());
            return new ApiResponse(-1, null);
        }
    }

    /**
     * Classe interna para representar a resposta da API
     */
    public static class ApiResponse {
        private final int statusCode;
        private final String body;

        public ApiResponse(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getBody() {
            return body;
        }

        public boolean isSuccess() {
            return statusCode >= 200 && statusCode < 300;
        }
    }
}
