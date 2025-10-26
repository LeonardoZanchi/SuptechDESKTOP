package br.com.suptec.controllers.helpers;

import br.com.suptec.models.Usuario;
import br.com.suptec.services.AuthService;
import br.com.suptec.services.UserManagementService;
import br.com.suptec.utils.AlertUtils;

/**
 * Classe responsável por gerenciar as ações sobre usuários
 * (Criar, Editar, Excluir, Visualizar)
 * Aplica o princípio Single Responsibility (SRP)
 */
public class UserActionHandler {
    
    private final Runnable onDataChanged;
    private final UserManagementService userManagementService;

    public UserActionHandler(Runnable onDataChanged) {
        this.onDataChanged = onDataChanged;
        this.userManagementService = UserManagementService.getInstance();
    }

    /**
     * Abre interface para criar novo usuário
     */
    public void handleNovo() {
        AlertUtils.showInfo("Novo Usuário", "Funcionalidade de cadastro em desenvolvimento.");
        // TODO: Abrir modal ou nova tela para cadastro
        // Após cadastro bem-sucedido, chamar: onDataChanged.run();
    }

    /**
     * Abre interface para editar usuário
     */
    public void handleEditar(Usuario usuario) {
        if (usuario == null) {
            AlertUtils.showWarning("Aviso", "Selecione um usuário para editar.");
            return;
        }

        try {
            // Carregar o FXML da tela de edição
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/fxml/UserEditView.fxml")
            );
            javafx.scene.Parent root = loader.load();

            // Obter o controller e configurar o usuário
            br.com.suptec.controllers.UserEditController controller = loader.getController();
            controller.setUsuario(usuario);
            controller.setOnSaveCallback(onDataChanged);

            // Criar e configurar a janela modal
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Editar Usuário - SUPTEC");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            
            // Adicionar ícone da empresa
            try {
                javafx.scene.image.Image icon = new javafx.scene.image.Image(
                    getClass().getResourceAsStream("/images/LogoPrincipal.jpg")
                );
                stage.getIcons().add(icon);
            } catch (Exception iconError) {
                System.err.println("Aviso: Não foi possível carregar o ícone da janela");
            }
            
            stage.showAndWait();

        } catch (Exception e) {
            System.err.println("Erro ao abrir tela de edição: " + e.getMessage());
            e.printStackTrace();
            AlertUtils.showError(
                "Erro",
                "Não foi possível abrir a tela de edição.\n\nErro: " + e.getMessage()
            );
        }
    }

    /**
     * Exclui o usuário após confirmação
     * Conectado à API para exclusão real
     */
    public void handleExcluir(Usuario usuario) {
        if (usuario == null) {
            AlertUtils.showWarning("Aviso", "Selecione um usuário para excluir.");
            return;
        }

        // Verificar se o usuário tem ID válido
        if (usuario.getId() == null || usuario.getId().trim().isEmpty()) {
            AlertUtils.showError(
                "Erro de Dados",
                "Não é possível excluir este usuário porque ele não possui um ID válido.\n\n" +
                "Isso pode indicar um problema na sincronização com a API.\n" +
                "Tente atualizar a lista de usuários."
            );
            System.err.println("✗ Tentativa de exclusão de usuário sem ID válido: " + usuario.getNome());
            return;
        }

        // Log detalhado para debug
        System.out.println("\n=== DEBUG: DADOS DO USUÁRIO PARA EXCLUSÃO ===");
        System.out.println("ID: " + usuario.getId());
        System.out.println("Nome: " + usuario.getNome());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Tipo: " + usuario.getTipo());
        System.out.println("===============================================\n");

        // Confirmação com mais detalhes
        boolean confirmado = AlertUtils.showConfirmation(
            "Confirmar Exclusão",
            String.format(
                "Deseja realmente excluir o usuário?\n\n" +
                "Nome: %s\n" +
                "Email: %s\n" +
                "Tipo: %s\n" +
                "ID: %s\n\n" +
                "Esta ação não pode ser desfeita!",
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipoDescricao(),
                usuario.getId()
            )
        );
        
        if (!confirmado) {
            System.out.println("⚠ Exclusão cancelada pelo usuário");
            return;
        }

        // Verificar se está tentando excluir o próprio usuário
        AuthService authService = AuthService.getInstance();
        String emailLogado = authService.getEmailUsuarioLogado();
        
        if (emailLogado != null && emailLogado.equalsIgnoreCase(usuario.getEmail())) {
            AlertUtils.showWarning(
                "Operação Não Permitida",
                "Você não pode excluir o próprio usuário que está logado no sistema.\n\n" +
                "Por favor, peça a outro administrador para realizar esta operação."
            );
            System.out.println("⚠ Tentativa de excluir o próprio usuário bloqueada");
            return;
        }

        // Tentar excluir via API
        System.out.println("\n=== INICIANDO PROCESSO DE EXCLUSÃO ===");
        boolean sucesso = userManagementService.excluirUsuario(usuario);
        
        if (sucesso) {
            AlertUtils.showSuccess(
                "Sucesso", 
                String.format("Usuário '%s' foi excluído com sucesso!", usuario.getNome())
            );
            
            // Recarregar lista de usuários
            System.out.println("🔄 Recarregando lista de usuários...");
            onDataChanged.run();
            
            System.out.println("=== EXCLUSÃO CONCLUÍDA COM SUCESSO ===\n");
        } else {
            AlertUtils.showError(
                "Erro ao Excluir", 
                String.format(
                    "Não foi possível excluir o usuário '%s'.\n\n" +
                    "Verifique:\n" +
                    "• Conexão com a API\n" +
                    "• Se o usuário ainda existe\n" +
                    "• Logs do console para mais detalhes\n\n" +
                    "Se o problema persistir, contate o administrador.",
                    usuario.getNome()
                )
            );
            
            System.out.println("=== EXCLUSÃO FALHOU ===\n");
        }
    }

    /**
     * Visualiza os detalhes do usuário
     */
    public void handleVisualizarDetalhes(Usuario usuario) {
        if (usuario == null) {
            AlertUtils.showWarning("Aviso", "Selecione um usuário para visualizar os detalhes.");
            return;
        }

        String detalhes = formatarDetalhesUsuario(usuario);
        AlertUtils.showInfo("Detalhes do Usuário", detalhes);
    }

    /**
     * Formata os detalhes do usuário para exibição
     */
    private String formatarDetalhesUsuario(Usuario usuario) {
        return String.format(
            "Nome: %s\nEmail: %s\nTelefone: %s\nSetor: %s\nTipo: %s",
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getTelefone(),
            usuario.getSetor(),
            usuario.getTipoDescricao()
        );
    }
}
