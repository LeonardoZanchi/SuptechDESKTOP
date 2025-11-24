package br.com.suptec.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Modelo que representa um chamado (ticket) do sistema
 * Contém apenas os campos retornados pela API de Chamados
 */
public class Chamado {
    private String chamadoID; // GUID da API
    private String nomeDoUsuario;
    private String emailDoUsuario;
    private String setorDoUsuario;
    private String titulo;
    private String descricao;
    private String prioridade; // "Baixa", "Media", "Alta"
    private String status; // novo campo: ex: "ABERTO", "FECHADO", etc.
    private String respostaDoTecnico; // Resposta do técnico ao chamado
    private String tecnicoResponsavel; // Nome do técnico responsável pelo chamado
    private LocalDateTime dataAbertura;

    // Enum para prioridades
    public enum Prioridade {
        BAIXA("Baixa"),
        MEDIA("Media"),
        ALTA("Alta");

        private final String descricao;

        Prioridade(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }

        @Override
        public String toString() {
            return descricao;
        }
    }

    // Construtores
    public Chamado() {}

    public Chamado(String chamadoID, String nomeDoUsuario, String emailDoUsuario, 
                   String setorDoUsuario, String titulo, String descricao, 
                   String prioridade, String status, String respostaDoTecnico, 
                   String tecnicoResponsavel, LocalDateTime dataAbertura) {
        this.chamadoID = chamadoID;
        this.nomeDoUsuario = nomeDoUsuario;
        this.emailDoUsuario = emailDoUsuario;
        this.setorDoUsuario = setorDoUsuario;
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.status = status;
        this.respostaDoTecnico = respostaDoTecnico;
        this.tecnicoResponsavel = tecnicoResponsavel;
        this.dataAbertura = dataAbertura;
    }

    // Getters e Setters
    public String getChamadoID() { return chamadoID; }
    public void setChamadoID(String chamadoID) { this.chamadoID = chamadoID; }

    public String getNomeDoUsuario() { return nomeDoUsuario; }
    public void setNomeDoUsuario(String nomeDoUsuario) { this.nomeDoUsuario = nomeDoUsuario; }

    public String getEmailDoUsuario() { return emailDoUsuario; }
    public void setEmailDoUsuario(String emailDoUsuario) { this.emailDoUsuario = emailDoUsuario; }

    public String getSetorDoUsuario() { return setorDoUsuario; }
    public void setSetorDoUsuario(String setorDoUsuario) { this.setorDoUsuario = setorDoUsuario; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getPrioridade() { return prioridade; }
    public void setPrioridade(String prioridade) { this.prioridade = prioridade; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRespostaDoTecnico() { return respostaDoTecnico; }
    public void setRespostaDoTecnico(String respostaDoTecnico) { this.respostaDoTecnico = respostaDoTecnico; }

    public String getTecnicoResponsavel() { return tecnicoResponsavel; }
    public void setTecnicoResponsavel(String tecnicoResponsavel) { this.tecnicoResponsavel = tecnicoResponsavel; }

    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDateTime dataAbertura) { this.dataAbertura = dataAbertura; }

    // Métodos auxiliares
    public String getDataAberturaFormatada() {
        if (dataAbertura == null) return "N/A";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dataAbertura.format(formatter);
    }

    public String getEmailFormatado() {
        return (emailDoUsuario != null && !emailDoUsuario.trim().isEmpty()) ? emailDoUsuario : "N/A";
    }

    public String getSetorFormatado() {
        return (setorDoUsuario != null && !setorDoUsuario.trim().isEmpty()) ? setorDoUsuario : "N/A";
    }

    public String getRespostaDoTecnicoFormatada() {
        return (respostaDoTecnico != null && !respostaDoTecnico.trim().isEmpty()) ? respostaDoTecnico : "Sem resposta";
    }

    public String getTecnicoResponsavelFormatado() {
        return (tecnicoResponsavel != null && !tecnicoResponsavel.trim().isEmpty()) ? tecnicoResponsavel : "Não atribuído";
    }
}
