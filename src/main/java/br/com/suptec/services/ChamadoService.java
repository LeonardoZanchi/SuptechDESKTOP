package br.com.suptec.services;

import java.time.LocalDateTime;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.suptec.models.Chamado;
import br.com.suptec.services.ApiService.ApiResponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Serviço responsável pelo gerenciamento de chamados
 * Aplica o princípio Single Responsibility (SRP)
 */
public class ChamadoService {
    
    private static ChamadoService instance;
    private final ApiService apiService;

    private ChamadoService() {
        this.apiService = new ApiService();
    }
    
    public static ChamadoService getInstance() {
        if (instance == null) {
            instance = new ChamadoService();
        }
        return instance;
    }

    /**
     * Lista todos os chamados da API
     */
    public ObservableList<Chamado> listarChamados() {
        ObservableList<Chamado> chamados = FXCollections.observableArrayList();

        try {
            // Obter token de autenticação
            String token = AuthService.getInstance().getTokenAtual();
            if (token == null || token.isEmpty()) {
                System.err.println("Erro: Token de autenticação não encontrado. Faça login novamente.");
                return chamados;
            }

            System.out.println("Buscando chamados...");
            System.out.println("Endpoint: Chamado/ListarChamados");
            System.out.println("Token presente: " + (token.length() > 20 ? "Sim" : "Token muito curto"));

            ApiResponse response = apiService.getWithAuth("Chamado/ListarChamados", token);
            
            System.out.println("📥 Status recebido: " + response.getStatusCode());
            
            if (response.getStatusCode() == 200 && response.getBody() != null) {
                System.out.println("📦 Resposta completa da API:");
                System.out.println(response.getBody());
                System.out.println("=====================================");
                JsonArray jsonArray = JsonParser.parseString(response.getBody()).getAsJsonArray();
                
                for (JsonElement element : jsonArray) {
                    JsonObject obj = element.getAsJsonObject();
                    Chamado chamado = parseChamado(obj);
                    if (chamado != null) {
                        chamados.add(chamado);
                    }
                }
                
                System.out.println("Carregados " + chamados.size() + " chamados com sucesso");
            } else {
                System.err.println("Erro ao buscar chamados. Status: " + response.getStatusCode());
                System.err.println("Corpo da resposta: " + response.getBody());
                if (response.getStatusCode() == 401) {
                    System.err.println("Token inválido ou expirado. Faça login novamente.");
                } else if (response.getStatusCode() == 500) {
                    System.err.println("Erro no servidor. Verifique se a API está funcionando corretamente.");
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar chamados da API: " + e.getMessage());
            e.printStackTrace();
        }

        return chamados;
    }

    /**
     * Busca chamados por termo de pesquisa
     */
    public ObservableList<Chamado> buscarChamados(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return listarChamados();
        }

        ObservableList<Chamado> todosChamados = listarChamados();
        ObservableList<Chamado> resultados = FXCollections.observableArrayList();
        
        String termoBusca = termo.toLowerCase().trim();

        for (Chamado chamado : todosChamados) {
            if (matchesBusca(chamado, termoBusca)) {
                resultados.add(chamado);
            }
        }

        return resultados;
    }

    private boolean matchesBusca(Chamado chamado, String termo) {
        return (chamado.getTitulo() != null && chamado.getTitulo().toLowerCase().contains(termo)) ||
               (chamado.getDescricao() != null && chamado.getDescricao().toLowerCase().contains(termo)) ||
               (chamado.getNomeDoUsuario() != null && chamado.getNomeDoUsuario().toLowerCase().contains(termo)) ||
               (chamado.getEmailDoUsuario() != null && chamado.getEmailDoUsuario().toLowerCase().contains(termo)) ||
               (chamado.getSetorDoUsuario() != null && chamado.getSetorDoUsuario().toLowerCase().contains(termo)) ||
               (chamado.getPrioridade() != null && chamado.getPrioridade().toLowerCase().contains(termo));
    }

    /**
     * Filtra chamados por prioridade
     */
    public ObservableList<Chamado> filtrarPorPrioridade(String prioridade) {
        if (prioridade == null || prioridade.trim().isEmpty() || prioridade.equalsIgnoreCase("Todas")) {
            return listarChamados();
        }

        ObservableList<Chamado> todosChamados = listarChamados();
        ObservableList<Chamado> resultados = FXCollections.observableArrayList();

        for (Chamado chamado : todosChamados) {
            if (chamado.getPrioridade() != null && 
                chamado.getPrioridade().equalsIgnoreCase(prioridade)) {
                resultados.add(chamado);
            }
        }

        return resultados;
    }

    /**
     * Exclui um chamado
     */
    public boolean excluirChamado(Chamado chamado) {
        if (chamado == null || chamado.getChamadoID() == null) {
            System.err.println("Chamado inválido para exclusão");
            return false;
        }

        try {
            // Obter token de autenticação
            String token = AuthService.getInstance().getTokenAtual();
            if (token == null || token.isEmpty()) {
                System.err.println("✗ Token de autenticação não encontrado. Faça login novamente.");
                return false;
            }

            String endpoint = "Chamado/Excluir/" + chamado.getChamadoID();
            ApiResponse response = apiService.deleteWithAuth(endpoint, token);
            
            if (response.getStatusCode() == 200) {
                System.out.println("Chamado ID " + chamado.getChamadoID() + " excluído com sucesso");
                return true;
            } else {
                System.err.println("Erro ao excluir chamado. Status: " + response.getStatusCode());
                if (response.getStatusCode() == 401) {
                    System.err.println("✗ Token inválido ou expirado. Faça login novamente.");
                }
                return false;
            }
        } catch (Exception e) {
            System.err.println("Erro ao excluir chamado: " + e.getMessage());
            return false;
        }
    }

    /**
     * Atualiza um chamado existente
     */
    public boolean atualizarChamado(Chamado chamado) {
        if (chamado == null || chamado.getChamadoID() == null) {
            System.err.println("Chamado inválido para atualização");
            return false;
        }

        try {
            // Obter token de autenticação
            String token = AuthService.getInstance().getTokenAtual();
            if (token == null || token.isEmpty()) {
                System.err.println("✗ Token de autenticação não encontrado. Faça login novamente.");
                return false;
            }

            String endpoint = "Chamado/Editar/" + chamado.getChamadoID();
            String jsonBody = buildChamadoJson(chamado);
            
            ApiResponse response = apiService.putWithAuth(endpoint, jsonBody, token);
            
            if (response.getStatusCode() == 200) {
                System.out.println("Chamado ID " + chamado.getChamadoID() + " atualizado com sucesso");
                return true;
            } else {
                System.err.println("Erro ao atualizar chamado. Status: " + response.getStatusCode());
                if (response.getStatusCode() == 401) {
                    System.err.println("✗ Token inválido ou expirado. Faça login novamente.");
                }
                return false;
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar chamado: " + e.getMessage());
            return false;
        }
    }

    private Chamado parseChamado(JsonObject obj) {
        try {
            // Debug: mostrar todos os campos recebidos
            System.out.println("=== CAMPOS DO CHAMADO RECEBIDOS DA API ===");
            for (String key : obj.keySet()) {
                System.out.println("Campo: " + key + " = " + obj.get(key));
            }
            System.out.println("=========================================");
            
            Chamado chamado = new Chamado();
            
            if (obj.has("chamadoID") && !obj.get("chamadoID").isJsonNull()) {
                chamado.setChamadoID(obj.get("chamadoID").getAsString());
            }
            if (obj.has("nomeDoUsuario") && !obj.get("nomeDoUsuario").isJsonNull()) {
                chamado.setNomeDoUsuario(obj.get("nomeDoUsuario").getAsString());
            }
            if (obj.has("emailDoUsuario") && !obj.get("emailDoUsuario").isJsonNull()) {
                chamado.setEmailDoUsuario(obj.get("emailDoUsuario").getAsString());
            }
            if (obj.has("setorDoUsuario") && !obj.get("setorDoUsuario").isJsonNull()) {
                chamado.setSetorDoUsuario(obj.get("setorDoUsuario").getAsString());
            }
            if (obj.has("titulo") && !obj.get("titulo").isJsonNull()) {
                chamado.setTitulo(obj.get("titulo").getAsString());
            }
            if (obj.has("descricao") && !obj.get("descricao").isJsonNull()) {
                chamado.setDescricao(obj.get("descricao").getAsString());
            }
            if (obj.has("prioridade") && !obj.get("prioridade").isJsonNull()) {
                chamado.setPrioridade(obj.get("prioridade").getAsString());
            }
            // Novo campo status vindo da API
            if (obj.has("status") && !obj.get("status").isJsonNull()) {
                chamado.setStatus(obj.get("status").getAsString());
            }
            // Campo resposta do técnico (testar múltiplas variações de nome)
            boolean respostaEncontrada = false;
            if (obj.has("respostaTecnico") && !obj.get("respostaTecnico").isJsonNull()) {
                String resposta = obj.get("respostaTecnico").getAsString();
                System.out.println("DEBUG - Resposta do Técnico encontrada (respostaTecnico): " + resposta);
                chamado.setRespostaDoTecnico(resposta);
                respostaEncontrada = true;
            } else if (obj.has("respostaDoTecnico") && !obj.get("respostaDoTecnico").isJsonNull()) {
                String resposta = obj.get("respostaDoTecnico").getAsString();
                System.out.println("DEBUG - Resposta do Técnico encontrada (respostaDoTecnico): " + resposta);
                chamado.setRespostaDoTecnico(resposta);
                respostaEncontrada = true;
            } else if (obj.has("resposta") && !obj.get("resposta").isJsonNull()) {
                String resposta = obj.get("resposta").getAsString();
                System.out.println("DEBUG - Resposta do Técnico encontrada (resposta): " + resposta);
                chamado.setRespostaDoTecnico(resposta);
                respostaEncontrada = true;
            }
            
            if (!respostaEncontrada) {
                System.out.println("DEBUG - Resposta do Técnico não encontrada em nenhuma variação (respostaTecnico, respostaDoTecnico, resposta)");
            }
            // Campo técnico responsável (pode ser nomeDoTecnico, tecnicoResponsavel, etc)
            if (obj.has("nomeDoTecnico") && !obj.get("nomeDoTecnico").isJsonNull()) {
                String tecnico = obj.get("nomeDoTecnico").getAsString();
                chamado.setTecnicoResponsavel(tecnico);
            } else if (obj.has("tecnicoResponsavel") && !obj.get("tecnicoResponsavel").isJsonNull()) {
                String tecnico = obj.get("tecnicoResponsavel").getAsString();
                chamado.setTecnicoResponsavel(tecnico);
            } else if (obj.has("tecnico") && !obj.get("tecnico").isJsonNull()) {
                String tecnico = obj.get("tecnico").getAsString();
                chamado.setTecnicoResponsavel(tecnico);
            }
            if (obj.has("dataAbertura") && !obj.get("dataAbertura").isJsonNull()) {
                String dataStr = obj.get("dataAbertura").getAsString();
                chamado.setDataAbertura(LocalDateTime.parse(dataStr));
            }
            
            return chamado;
        } catch (Exception e) {
            System.err.println("Erro ao parsear chamado: " + e.getMessage());
            return null;
        }
    }

    private String buildChamadoJson(Chamado chamado) {
        JsonObject json = new JsonObject();
        
        if (chamado.getChamadoID() != null) {
            json.addProperty("chamadoID", chamado.getChamadoID());
        }
        json.addProperty("titulo", chamado.getTitulo());
        json.addProperty("descricao", chamado.getDescricao());
        json.addProperty("prioridade", chamado.getPrioridade());
        if (chamado.getStatus() != null) {
            json.addProperty("status", chamado.getStatus());
        }
        // Adicionar técnico responsável se houver
        if (chamado.getTecnicoResponsavel() != null && !chamado.getTecnicoResponsavel().trim().isEmpty()) {
            json.addProperty("tecnicoResponsavel", chamado.getTecnicoResponsavel());
        }
        // Adicionar resposta do técnico se houver (campo da API: respostaTecnico)
        if (chamado.getRespostaDoTecnico() != null) {
            json.addProperty("respostaTecnico", chamado.getRespostaDoTecnico());
        }
        
        return json.toString();
    }
}
