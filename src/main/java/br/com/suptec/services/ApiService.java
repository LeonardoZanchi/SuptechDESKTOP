package br.com.suptec.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import br.com.suptec.utils.ConfigLoader;

public class ApiService {
    private final String BASE_URL;
    private final HttpClient client;

    public ApiService() {
        this.BASE_URL = ConfigLoader.getInstance().getApiBaseUrl();
        System.out.println("🔧 ApiService inicializado com URL: " + BASE_URL);
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(ConfigLoader.getInstance().getApiTimeout()))
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
            String url = BASE_URL + endpoint;
            
            System.out.println("\n🌐 === POST REQUEST ===");
            System.out.println("URL: " + url);
            System.out.println("Content-Type: application/json");
            System.out.println("Body: " + jsonBody);
            System.out.println("=====================\n");
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(30))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            System.out.println("📤 Enviando requisição...");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            System.out.println("\n📥 === POST RESPONSE ===");
            System.out.println("Status: " + response.statusCode());
            System.out.println("Body: " + (response.body() != null ? response.body() : "null"));
            System.out.println("=======================\n");
            
            return new ApiResponse(response.statusCode(), response.body());
        } catch (java.net.ConnectException e) {
            System.err.println("❌ ERRO DE CONEXÃO: Não foi possível conectar ao servidor");
            System.err.println("   URL tentada: " + BASE_URL + endpoint);
            System.err.println("   Verifique se a API está rodando");
            System.err.println("   Detalhes: " + e.getMessage());
            return new ApiResponse(-1, null);
        } catch (java.net.http.HttpTimeoutException e) {
            System.err.println("❌ TIMEOUT: A requisição demorou muito tempo");
            System.err.println("   URL: " + BASE_URL + endpoint);
            System.err.println("   Detalhes: " + e.getMessage());
            return new ApiResponse(-1, null);
        } catch (Exception e) {
            System.err.println("❌ ERRO NA REQUISIÇÃO POST");
            System.err.println("   Endpoint: " + endpoint);
            System.err.println("   Tipo: " + e.getClass().getName());
            System.err.println("   Mensagem: " + e.getMessage());
            e.printStackTrace();
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
     * Executa uma requisição GET para a API com token de autenticação
     * @param endpoint Endpoint da API (sem a barra inicial)
     * @param token Token JWT de autenticação
     * @return Response com status code e body
     */
    public ApiResponse getWithAuth(String endpoint, String token) {
        try {
            String url = BASE_URL + endpoint;
            System.out.println("GET Request: " + url);
            System.out.println("Authorization: Bearer " + (token != null ? token.substring(0, Math.min(20, token.length())) + "..." : "null"));
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .timeout(Duration.ofSeconds(30))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("📡 Response Status: " + response.statusCode());
            return new ApiResponse(response.statusCode(), response.body());
        } catch (Exception e) {
            System.err.println("Erro na requisição GET com autenticação para " + endpoint + ": " + e.getMessage());
            e.printStackTrace();
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
     * Executa uma requisição DELETE para a API com token de autenticação
     * @param endpoint Endpoint da API com query params
     * @param token Token JWT de autenticação
     * @return Response com status code e body
     */
    public ApiResponse deleteWithAuth(String endpoint, String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .timeout(Duration.ofSeconds(30))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(response.statusCode(), response.body());
        } catch (Exception e) {
            System.err.println("Erro na requisição DELETE com autenticação para " + endpoint + ": " + e.getMessage());
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
     * Executa uma requisição PUT para a API com token de autenticação
     * @param endpoint Endpoint da API (sem a barra inicial)
     * @param jsonBody Corpo da requisição em JSON
     * @param token Token JWT de autenticação
     * @return Response com status code e body
     */
    public ApiResponse putWithAuth(String endpoint, String jsonBody, String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .timeout(Duration.ofSeconds(30))
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(response.statusCode(), response.body());
        } catch (Exception e) {
            System.err.println("Erro na requisição PUT com autenticação para " + endpoint + ": " + e.getMessage());
            return new ApiResponse(-1, null);
        }
    }

    /**
     * Executa uma requisição POST para a API com token de autenticação
     * @param endpoint Endpoint da API (sem a barra inicial)
     * @param jsonBody Corpo da requisição em JSON
     * @param token Token JWT de autenticação
     * @return Response com status code e body
     */
    public ApiResponse postWithAuth(String endpoint, String jsonBody, String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .timeout(Duration.ofSeconds(30))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(response.statusCode(), response.body());
        } catch (Exception e) {
            System.err.println("Erro na requisição POST com autenticação para " + endpoint + ": " + e.getMessage());
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
