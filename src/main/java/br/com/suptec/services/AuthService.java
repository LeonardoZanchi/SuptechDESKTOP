package br.com.suptec.services;

import java.util.Base64;

import br.com.suptec.models.api.LoginRequest;
import br.com.suptec.models.api.LoginResponse;
import br.com.suptec.services.ApiService.ApiResponse;
import br.com.suptec.utils.JsonUtils;

/**
 * Serviço responsável pela autenticação de usuários
 * Aplica o princípio Single Responsibility (SRP)
 */
public class AuthService {
    
    private static AuthService instance;
    
    private final ApiService apiService;
    private String tokenAtual;
    private String emailUsuarioLogado;
    private String nomeUsuarioLogado;

    private AuthService() {
        this.apiService = new ApiService();
    }
    
    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    /**
     * Valida o login através da API (apenas gerentes podem acessar o desktop)
     * @param email Email do usuário
     * @param senha Senha do usuário
     * @return true se o login for válido e o usuário for gerente, false caso contrário
     */
    public boolean validarLogin(String email, String senha) {
        try {
            // Criar requisição de login
            LoginRequest loginRequest = new LoginRequest(email, senha);
            String jsonRequest = JsonUtils.toJson(loginRequest);
            
            if (jsonRequest == null) {
                System.err.println("Erro ao serializar requisição de login");
                return false;
            }

            System.out.println("🔐 Validando login de gerente para acesso desktop...");
            System.out.println("Enviando requisição: " + jsonRequest);

            // Fazer chamada para API específica de desktop (só gerentes)
            // Codificar o espaço na URL como %20
            ApiResponse response = apiService.post("AuthDesktop/Login%20Desktop", jsonRequest);
            
            System.out.println("Status da resposta: " + response.getStatusCode());
            System.out.println("Corpo da resposta: " + response.getBody());
            
            if (response.getStatusCode() == -1) {
                System.err.println("✗ Erro de conexão com a API");
                return false;
            }

            if (!response.isSuccess()) {
                System.err.println("✗ Login falhou. Status: " + response.getStatusCode());
                if (response.getStatusCode() == 401) {
                    System.err.println("✗ Acesso negado: Usuário não é gerente ou credenciais inválidas");
                } else if (response.getStatusCode() == 404) {
                    System.err.println("✗ Usuário não encontrado como gerente");
                }
                if (response.getBody() != null) {
                    System.err.println("Resposta da API: " + response.getBody());
                }
                return false;
            }

            // Processar resposta
            if (response.getBody() != null && !response.getBody().trim().isEmpty()) {
                LoginResponse loginResponse = JsonUtils.fromJson(response.getBody(), LoginResponse.class);
                
                if (loginResponse != null && loginResponse.isSuccess()) {
                    this.tokenAtual = loginResponse.getToken();
                    this.emailUsuarioLogado = email;
                    this.nomeUsuarioLogado = extrairNomeDoToken(loginResponse.getToken());
                    
                    System.out.println("✓ Login de gerente realizado com sucesso!");
                    System.out.println("✓ Token recebido para usuário: " + this.nomeUsuarioLogado);
                    System.out.println("✓ Acesso desktop autorizado");
                    
                    return true;
                }
            }

            System.err.println("✗ Resposta da API não contém token válido para gerente");
            return false;

        } catch (Exception e) {
            System.err.println("✗ Erro durante validação de login: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extrai o nome do usuário do token JWT
     */
    private String extrairNomeDoToken(String token) {
        try {
            if (token == null || token.isEmpty()) {
                return "Usuário";
            }

            // JWT tem formato: header.payload.signature
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                return "Usuário";
            }

            // Decodificar o payload (segunda parte)
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            
            // Procurar pelo campo "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name"
            String nameKey = "\"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name\"";
            int nameIndex = payload.indexOf(nameKey);
            if (nameIndex != -1) {
                // Encontrar o valor após os dois pontos
                int colonIndex = payload.indexOf(":", nameIndex + nameKey.length());
                if (colonIndex != -1) {
                    int commaIndex = payload.indexOf(",", colonIndex);
                    if (commaIndex == -1) {
                        commaIndex = payload.indexOf("}", colonIndex);
                    }
                    if (commaIndex != -1) {
                        String nameValue = payload.substring(colonIndex + 1, commaIndex).trim();
                        if (nameValue.startsWith("\"") && nameValue.endsWith("\"")) {
                            nameValue = nameValue.substring(1, nameValue.length() - 1);
                        }
                        return nameValue;
                    }
                }
            }

            // Fallback: tentar extrair do email
            if (emailUsuarioLogado != null) {
                String nome = emailUsuarioLogado.split("@")[0];
                return nome.substring(0, 1).toUpperCase() + nome.substring(1);
            }

            return "Usuário";
        } catch (Exception e) {
            System.err.println("Erro ao extrair nome do token: " + e.getMessage());
            if (emailUsuarioLogado != null) {
                String nome = emailUsuarioLogado.split("@")[0];
                return nome.substring(0, 1).toUpperCase() + nome.substring(1);
            }
            return "Usuário";
        }
    }

    /**
     * Retorna o token JWT do último login realizado com sucesso
     */
    public String getTokenAtual() {
        return tokenAtual;
    }

    /**
     * Retorna o email do usuário logado
     */
    public String getEmailUsuarioLogado() {
        return emailUsuarioLogado != null ? emailUsuarioLogado : "Usuário";
    }

    /**
     * Retorna o nome de exibição do usuário (extraído do token JWT)
     */
    public String getNomeUsuarioLogado() {
        return nomeUsuarioLogado != null ? nomeUsuarioLogado : "Usuário";
    }

    /**
     * Limpa os dados de autenticação (logout)
     */
    public void logout() {
        this.tokenAtual = null;
        this.emailUsuarioLogado = null;
        this.nomeUsuarioLogado = null;
    }
}
