package br.com.suptec.models.api;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo de resposta da API para Usuários comuns
 * Endpoint: GET /api/Usuario/Listar
 */
public class UsuarioResponse {
    @SerializedName("usuarioID")
    private String id;
    private String nome;
    private String email;
    private String telefone;
    private String setor;

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getSetor() { return setor; }
    public void setSetor(String setor) { this.setor = setor; }
}
