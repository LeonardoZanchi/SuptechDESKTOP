package br.com.suptec.models.api;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo de resposta da API para Técnicos
 * Endpoint: GET /api/Tecnico/Listar
 */
public class TecnicoResponse {
    @SerializedName("tecnicoID")
    private String id;
    private String nome;
    private String email;
    private String telefone;
    private String especialidade;

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
}
