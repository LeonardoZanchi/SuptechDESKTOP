package br.com.suptec.services;

import br.com.suptec.models.api.GerenteRequest;
import br.com.suptec.models.api.TecnicoRequest;
import br.com.suptec.models.api.UsuarioRequest;
import br.com.suptec.services.ApiService.ApiResponse;
import br.com.suptec.utils.JsonUtils;

/**
 * Serviço responsável pelo cadastro/registro de novos usuários
 * Aplica o princípio Single Responsibility (SRP)
 * 
 * Separado do UserManagementService para manter responsabilidades distintas:
 * - UserManagementService: Listagem e consulta
 * - UserRegistrationService: Cadastro e criação
 */
public class UserRegistrationService {
    
    private static UserRegistrationService instance;
    private final ApiService apiService;

    private UserRegistrationService() {
        this.apiService = new ApiService();
    }
    
    public static UserRegistrationService getInstance() {
        if (instance == null) {
            instance = new UserRegistrationService();
        }
        return instance;
    }

    /**
     * Adiciona um novo gerente
     * @param nome Nome completo do gerente
     * @param email Email válido
     * @param senha Senha de acesso
     * @param telefone Telefone de contato
     * @param setor Setor de atuação
     * @return true se o cadastro foi realizado com sucesso
     */
    public boolean adicionarGerente(String nome, String email, String senha, String telefone, String setor) {
        try {
            GerenteRequest request = new GerenteRequest(nome, email, senha, telefone, setor);
            String jsonRequest = JsonUtils.toJson(request);
            
            if (jsonRequest == null) {
                System.err.println("Erro ao serializar requisição de gerente");
                return false;
            }

            System.out.println("Cadastrando gerente: " + nome);
            ApiResponse response = apiService.post("Gerente/Adicionar", jsonRequest);
            
            if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
                System.out.println("✓ Gerente cadastrado com sucesso!");
                return true;
            } else {
                System.err.println("✗ Erro ao cadastrar gerente. Status: " + response.getStatusCode());
                if (response.getBody() != null) {
                    System.err.println("Detalhes: " + response.getBody());
                }
                return false;
            }
        } catch (Exception e) {
            System.err.println("✗ Exceção ao cadastrar gerente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Adiciona um novo técnico
     * @param nome Nome completo do técnico
     * @param email Email válido
     * @param senha Senha de acesso
     * @param telefone Telefone de contato
     * @param especialidade Especialidade técnica
     * @return true se o cadastro foi realizado com sucesso
     */
    public boolean adicionarTecnico(String nome, String email, String senha, String telefone, String especialidade) {
        try {
            TecnicoRequest request = new TecnicoRequest(nome, email, senha, telefone, especialidade);
            String jsonRequest = JsonUtils.toJson(request);
            
            if (jsonRequest == null) {
                System.err.println("Erro ao serializar requisição de técnico");
                return false;
            }

            System.out.println("Cadastrando técnico: " + nome);
            ApiResponse response = apiService.post("Tecnico/Adicionar", jsonRequest);
            
            if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
                System.out.println("✓ Técnico cadastrado com sucesso!");
                return true;
            } else {
                System.err.println("✗ Erro ao cadastrar técnico. Status: " + response.getStatusCode());
                if (response.getBody() != null) {
                    System.err.println("Detalhes: " + response.getBody());
                }
                return false;
            }
        } catch (Exception e) {
            System.err.println("✗ Exceção ao cadastrar técnico: " + e.getMessage());
            return false;
        }
    }

    /**
     * Adiciona um novo usuário comum
     * @param nome Nome completo do usuário
     * @param email Email válido
     * @param senha Senha de acesso
     * @param telefone Telefone de contato
     * @param setor Setor de atuação
     * @return true se o cadastro foi realizado com sucesso
     */
    public boolean adicionarUsuario(String nome, String email, String senha, String telefone, String setor) {
        try {
            UsuarioRequest request = new UsuarioRequest(nome, email, senha, telefone, setor);
            String jsonRequest = JsonUtils.toJson(request);
            
            if (jsonRequest == null) {
                System.err.println("Erro ao serializar requisição de usuário");
                return false;
            }

            System.out.println("Cadastrando usuário: " + nome);
            ApiResponse response = apiService.post("Usuario/Adicionar", jsonRequest);
            
            if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
                System.out.println("✓ Usuário cadastrado com sucesso!");
                return true;
            } else {
                System.err.println("✗ Erro ao cadastrar usuário. Status: " + response.getStatusCode());
                if (response.getBody() != null) {
                    System.err.println("Detalhes: " + response.getBody());
                }
                return false;
            }
        } catch (Exception e) {
            System.err.println("✗ Exceção ao cadastrar usuário: " + e.getMessage());
            return false;
        }
    }
}
