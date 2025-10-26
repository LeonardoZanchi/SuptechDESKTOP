package br.com.suptec.services;

import java.util.ArrayList;
import java.util.List;

import br.com.suptec.models.Usuario;
import br.com.suptec.models.Usuario.TipoUsuario;
import br.com.suptec.models.api.GerenteResponse;
import br.com.suptec.models.api.TecnicoResponse;
import br.com.suptec.models.api.UsuarioResponse;
import br.com.suptec.services.ApiService.ApiResponse;
import br.com.suptec.utils.JsonUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Serviço responsável pelo gerenciamento de usuários (listagens e buscas)
 * Aplica o princípio Single Responsibility (SRP)
 */
public class UserManagementService {
    
    private static UserManagementService instance;
    private final ApiService apiService;

    private UserManagementService() {
        this.apiService = new ApiService();
    }
    
    public static UserManagementService getInstance() {
        if (instance == null) {
            instance = new UserManagementService();
        }
        return instance;
    }

    /**
     * Lista todos os usuários (gerentes, técnicos e usuários comuns) da API
     * @return Lista observável de usuários
     */
    public ObservableList<Usuario> listarUsuarios() {
        ObservableList<Usuario> usuarios = FXCollections.observableArrayList();

        try {
            // Buscar todos os tipos de usuários em paralelo
            usuarios.addAll(listarGerentes());
            usuarios.addAll(listarTecnicos());
            usuarios.addAll(listarUsuariosComuns());

        } catch (Exception e) {
            System.err.println("✗ Erro ao buscar usuários da API: " + e.getMessage());
        }

        return usuarios;
    }

    /**
     * Lista apenas gerentes
     */
    private List<Usuario> listarGerentes() {
        List<Usuario> gerentes = new ArrayList<>();
        
        try {
            ApiResponse response = apiService.get("Gerente/Listar");
            if (response.getStatusCode() == 200 && response.getBody() != null) {
                GerenteResponse[] gerentesResponse = JsonUtils.fromJson(response.getBody(), GerenteResponse[].class);
                if (gerentesResponse != null) {
                    for (GerenteResponse gerente : gerentesResponse) {
                        gerentes.add(mapearGerenteParaUsuario(gerente));
                    }
                }
            } else {
                System.err.println("Erro ao buscar gerentes. Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar gerentes: " + e.getMessage());
        }
        
        return gerentes;
    }

    /**
     * Lista apenas técnicos
     */
    private List<Usuario> listarTecnicos() {
        List<Usuario> tecnicos = new ArrayList<>();
        
        try {
            ApiResponse response = apiService.get("Tecnico/Listar");
            if (response.getStatusCode() == 200 && response.getBody() != null) {
                TecnicoResponse[] tecnicosResponse = JsonUtils.fromJson(response.getBody(), TecnicoResponse[].class);
                if (tecnicosResponse != null) {
                    for (TecnicoResponse tecnico : tecnicosResponse) {
                        tecnicos.add(mapearTecnicoParaUsuario(tecnico));
                    }
                }
            } else {
                System.err.println("Erro ao buscar técnicos. Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar técnicos: " + e.getMessage());
        }
        
        return tecnicos;
    }

    /**
     * Lista apenas usuários comuns
     */
    private List<Usuario> listarUsuariosComuns() {
        List<Usuario> usuariosComuns = new ArrayList<>();
        
        try {
            ApiResponse response = apiService.get("Usuario/Listar");
            if (response.getStatusCode() == 200 && response.getBody() != null) {
                UsuarioResponse[] usuariosResponse = JsonUtils.fromJson(response.getBody(), UsuarioResponse[].class);
                if (usuariosResponse != null) {
                    for (UsuarioResponse usuario : usuariosResponse) {
                        usuariosComuns.add(mapearUsuarioComumParaUsuario(usuario));
                    }
                }
            } else {
                System.err.println("Erro ao buscar usuários. Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar usuários comuns: " + e.getMessage());
        }
        
        return usuariosComuns;
    }

    /**
     * Mapeia GerenteResponse para Usuario
     */
    private Usuario mapearGerenteParaUsuario(GerenteResponse gerente) {
        Usuario usuario = new Usuario();
        usuario.setId(gerente.getId()); // Mantém o ID como String (GUID)
        usuario.setNome(gerente.getNome());
        usuario.setEmail(gerente.getEmail());
        usuario.setTelefone(gerente.getTelefone());
        usuario.setSetor(gerente.getSetor() != null ? gerente.getSetor() : "N/A");
        usuario.setTipo(TipoUsuario.GERENTE);
        return usuario;
    }

    /**
     * Mapeia TecnicoResponse para Usuario
     */
    private Usuario mapearTecnicoParaUsuario(TecnicoResponse tecnico) {
        Usuario usuario = new Usuario();
        usuario.setId(tecnico.getId()); // Mantém o ID como String (GUID)
        usuario.setNome(tecnico.getNome());
        usuario.setEmail(tecnico.getEmail());
        usuario.setTelefone(tecnico.getTelefone());
        usuario.setEspecialidade(tecnico.getEspecialidade() != null ? tecnico.getEspecialidade() : "N/A");
        usuario.setTipo(TipoUsuario.TECNICO);
        return usuario;
    }

    /**
     * Mapeia UsuarioResponse para Usuario
     */
    private Usuario mapearUsuarioComumParaUsuario(UsuarioResponse usuarioResponse) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioResponse.getId()); // Mantém o ID como String (GUID)
        usuario.setNome(usuarioResponse.getNome());
        usuario.setEmail(usuarioResponse.getEmail());
        usuario.setTelefone(usuarioResponse.getTelefone());
        usuario.setSetor(usuarioResponse.getSetor() != null ? usuarioResponse.getSetor() : "N/A");
        usuario.setTipo(TipoUsuario.USUARIO);
        return usuario;
    }

    /**
     * Busca usuários por nome, email, setor ou especialidade
     */
    public ObservableList<Usuario> buscarUsuarios(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return listarUsuarios();
        }

        String termoLower = termo.toLowerCase().trim();
        ObservableList<Usuario> todosUsuarios = listarUsuarios();
        List<Usuario> resultados = new ArrayList<>();

        for (Usuario usuario : todosUsuarios) {
            if (usuarioMatchesTermo(usuario, termoLower)) {
                resultados.add(usuario);
            }
        }

        return FXCollections.observableArrayList(resultados);
    }

    /**
     * Verifica se um usuário corresponde ao termo de busca
     */
    private boolean usuarioMatchesTermo(Usuario usuario, String termo) {
        boolean matches = usuario.getNome().toLowerCase().contains(termo) ||
            usuario.getEmail().toLowerCase().contains(termo) ||
            usuario.getTipoDescricao().toLowerCase().contains(termo);
        
        // Verificar setor ou especialidade dependendo do tipo
        if (!matches) {
            if (usuario.getSetor() != null && usuario.getSetor().toLowerCase().contains(termo)) {
                matches = true;
            } else if (usuario.getEspecialidade() != null && usuario.getEspecialidade().toLowerCase().contains(termo)) {
                matches = true;
            }
        }
        
        return matches;
    }

    /**
     * Exclui um usuário da API usando o endpoint correto baseado no tipo
     * @param usuario Usuário a ser excluído (com ID GUID)
     * @return true se a exclusão foi bem-sucedida, false caso contrário
     */
    public boolean excluirUsuario(Usuario usuario) {
        if (usuario == null || usuario.getId() == null || usuario.getId().isEmpty()) {
            System.err.println("✗ Usuário inválido para exclusão (ID nulo ou vazio)");
            return false;
        }

        // Verificar se está tentando excluir o próprio usuário logado
        AuthService authService = AuthService.getInstance();
        String emailLogado = authService.getEmailUsuarioLogado();
        
        if (emailLogado != null && emailLogado.equalsIgnoreCase(usuario.getEmail())) {
            System.err.println("✗ Não é permitido excluir o próprio usuário logado");
            return false;
        }

        try {
            // Determinar endpoint baseado no tipo 
            String endpoint = obterEndpointExclusao(usuario.getTipo());
            // Formato correto: endpoint + "/" + ID (conforme especificado pela API)
            String url = endpoint + "/" + usuario.getId();
            
            System.out.println("🔗 Endpoint de exclusão: " + url);
            System.out.println("👤 Excluindo usuário: " + usuario.getNome() + " (ID: " + usuario.getId() + ")");
            
            ApiResponse response = apiService.delete(url);
            
            if (response.isSuccess()) {
                System.out.println("✅ Usuário excluído com sucesso da API");
                return true;
            } else {
                System.err.println("✗ Falha ao excluir usuário");
                System.err.println("  → Status HTTP: " + response.getStatusCode());
                if (response.getBody() != null) {
                    System.err.println("  → Resposta da API: " + response.getBody());
                }
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("✗ Erro ao excluir usuário: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retorna o endpoint correto de exclusão baseado no tipo de usuário
     */
    private String obterEndpointExclusao(TipoUsuario tipo) {
        switch (tipo) {
            case GERENTE:
                return "Gerente/Excluir";
            case TECNICO:
                return "Tecnico/Excluir";
            case USUARIO:
            default:
                return "Usuario/Excluir";
        }
    }

    /**
     * Atualiza um usuário na API usando o endpoint correto baseado no tipo
     * @param usuario Usuário com dados atualizados
     * @return true se a atualização foi bem-sucedida, false caso contrário
     */
    public boolean atualizarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getId() == null || usuario.getId().isEmpty()) {
            System.err.println("✗ Usuário inválido para atualização (ID nulo ou vazio)");
            return false;
        }

        try {
            // Determinar endpoint baseado no tipo (com ID na URL)
            String endpoint = obterEndpointAtualizacao(usuario.getTipo(), usuario.getId());
            
            // Montar JSON baseado no tipo de usuário
            String jsonBody = montarJsonAtualizacao(usuario);
            
            if (jsonBody == null) {
                System.err.println("✗ Erro ao montar JSON para atualização");
                return false;
            }

            System.out.println("🔄 Atualizando usuário via API...");
            System.out.println("  → Endpoint: " + endpoint);
            System.out.println("  → JSON: " + jsonBody);

            ApiResponse response = apiService.put(endpoint, jsonBody);
            
            if (response.isSuccess()) {
                System.out.println("✓ Usuário atualizado com sucesso!");
                return true;
            } else {
                System.err.println("✗ Falha ao atualizar usuário");
                System.err.println("  → Status HTTP: " + response.getStatusCode());
                if (response.getBody() != null) {
                    System.err.println("  → Resposta da API: " + response.getBody());
                }
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("✗ Erro ao atualizar usuário: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retorna o endpoint correto de atualização baseado no tipo de usuário
     * Formato: Tipo/Editar/ID
     */
    private String obterEndpointAtualizacao(TipoUsuario tipo, String id) {
        switch (tipo) {
            case GERENTE:
                return "Gerente/Editar/" + id;
            case TECNICO:
                return "Tecnico/Editar/" + id;
            case USUARIO:
            default:
                return "Usuario/Editar/" + id;
        }
    }

    /**
     * Monta o JSON para atualização baseado no tipo de usuário
     * Formato esperado pela API:
     * - Tecnico: { "nome", "email", "senha", "especialidade", "telefone" }
     * - Gerente/Usuario: { "nome", "email", "senha", "setor", "telefone" }
     */
    private String montarJsonAtualizacao(Usuario usuario) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        
        // Campos comuns obrigatórios
        json.append("\"nome\":\"").append(usuario.getNome()).append("\",");
        json.append("\"email\":\"").append(usuario.getEmail()).append("\",");
        json.append("\"telefone\":\"").append(usuario.getTelefone()).append("\"");
        
        // Senha (opcional - apenas se fornecida)
        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            json.append(",\"senha\":\"").append(usuario.getSenha()).append("\"");
        }
        
        // Campos específicos por tipo
        if (usuario.getTipo() == TipoUsuario.TECNICO) {
            // Para técnicos: especialidade
            if (usuario.getEspecialidade() != null && !usuario.getEspecialidade().isEmpty()) {
                json.append(",\"especialidade\":\"").append(usuario.getEspecialidade()).append("\"");
            }
        } else {
            // Para gerentes e usuários: setor
            if (usuario.getSetor() != null && !usuario.getSetor().isEmpty()) {
                json.append(",\"setor\":\"").append(usuario.getSetor()).append("\"");
            }
        }
        
        json.append("}");
        return json.toString();
    }
}
