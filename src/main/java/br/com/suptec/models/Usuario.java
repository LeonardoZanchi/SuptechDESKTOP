package br.com.suptec.models;

public class Usuario {
    private String id; // GUID da API (ex: "896c3968-c67f-493f-0089-08de0feba950")
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String setor;
    private String especialidade; // Para técnicos
    private TipoUsuario tipo;

    // Enum para tipos de usuário
    public enum TipoUsuario {
        USUARIO("Usuário"),
        TECNICO("Técnico"),
        GERENTE("Gerente");

        private final String descricao;

        TipoUsuario(String descricao) {
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
    public Usuario() {}

    public Usuario(String id, String nome, String email, String telefone, String setor, TipoUsuario tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.setor = setor;
        this.tipo = tipo;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getSetor() { return setor; }
    public void setSetor(String setor) { this.setor = setor; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public TipoUsuario getTipo() { return tipo; }
    public void setTipo(TipoUsuario tipo) { this.tipo = tipo; }

    // Método auxiliar para obter descrição do tipo
    public String getTipoDescricao() {
        return tipo != null ? tipo.getDescricao() : "";
    }
    
    /**
     * Retorna Setor para GERENTE e USUARIO, ou Especialidade para TECNICO
     */
    public String getSetorOuEspecialidade() {
        if (tipo == TipoUsuario.TECNICO) {
            return especialidade != null ? especialidade : "N/A";
        }
        return setor != null ? setor : "N/A";
    }
}
