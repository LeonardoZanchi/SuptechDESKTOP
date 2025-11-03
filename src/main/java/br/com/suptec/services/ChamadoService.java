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
            ApiResponse response = apiService.get("Chamado/Listar");
            
            if (response.getStatusCode() == 200 && response.getBody() != null) {
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
            String endpoint = "Chamado/Excluir/" + chamado.getChamadoID();
            ApiResponse response = apiService.delete(endpoint);
            
            if (response.getStatusCode() == 200) {
                System.out.println("Chamado ID " + chamado.getChamadoID() + " excluído com sucesso");
                return true;
            } else {
                System.err.println("Erro ao excluir chamado. Status: " + response.getStatusCode());
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
            String endpoint = "Chamado/Editar/" + chamado.getChamadoID();
            String jsonBody = buildChamadoJson(chamado);
            
            ApiResponse response = apiService.put(endpoint, jsonBody);
            
            if (response.getStatusCode() == 200) {
                System.out.println("Chamado ID " + chamado.getChamadoID() + " atualizado com sucesso");
                return true;
            } else {
                System.err.println("Erro ao atualizar chamado. Status: " + response.getStatusCode());
                return false;
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar chamado: " + e.getMessage());
            return false;
        }
    }

    private Chamado parseChamado(JsonObject obj) {
        try {
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
        
        return json.toString();
    }
}
