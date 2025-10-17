package br.com.suptec.services;

public class UsuarioService {

    // MVP: validação local (pode ser substituída por chamada à API)
    public boolean validarLogin(String usuario, String senha) {
        return "admin".equalsIgnoreCase(usuario) && "123".equals(senha);
    }
}
