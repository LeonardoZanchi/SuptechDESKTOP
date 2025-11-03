package br.com.suptec.controllers.helpers;

import br.com.suptec.models.Chamado;
import br.com.suptec.services.ChamadoService;
import br.com.suptec.utils.AlertUtils;

/**
 * Classe responsável por gerenciar as ações sobre chamados
 * Aplica o princípio Single Responsibility (SRP)
 */
public class ChamadoActionHandler {
    
    private final Runnable onDataChanged;
    private final ChamadoService chamadoService;

    public ChamadoActionHandler(Runnable onDataChanged) {
        this.onDataChanged = onDataChanged;
        this.chamadoService = ChamadoService.getInstance();
    }

    public void visualizarChamado(Chamado chamado) {
        if (chamado == null) {
            AlertUtils.showWarning("Aviso", "Selecione um chamado para visualizar.");
            return;
        }

        StringBuilder detalhes = new StringBuilder();
        detalhes.append("INFORMACOES DO CHAMADO\n\n");
        detalhes.append("ID: ").append(chamado.getChamadoID()).append("\n");
        detalhes.append("Titulo: ").append(chamado.getTitulo()).append("\n");
        detalhes.append("Prioridade: ").append(chamado.getPrioridade()).append("\n");
        detalhes.append("Data de Abertura: ").append(chamado.getDataAberturaFormatada()).append("\n\n");
        
        detalhes.append("USUARIO\n\n");
        detalhes.append("Nome: ").append(chamado.getNomeDoUsuario()).append("\n");
        detalhes.append("Email: ").append(chamado.getEmailFormatado()).append("\n");
        detalhes.append("Setor: ").append(chamado.getSetorFormatado()).append("\n\n");
        
        detalhes.append("DESCRICAO\n\n");
        detalhes.append(chamado.getDescricao() != null ? chamado.getDescricao() : "Sem descricao");

        AlertUtils.showInfo("Detalhes do Chamado", detalhes.toString());
    }

    public void editarChamado(Chamado chamado) {
        if (chamado == null) {
            AlertUtils.showWarning("Aviso", "Selecione um chamado para editar.");
            return;
        }
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/fxml/ChamadoEditView.fxml")
            );
            javafx.scene.Parent root = loader.load();

            br.com.suptec.controllers.ChamadoEditController controller = loader.getController();
            controller.setChamado(chamado);
            controller.setOnSaveCallback(onDataChanged);

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Editar Chamado - SUPTEC");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            try {
                javafx.scene.image.Image icon = new javafx.scene.image.Image(
                    getClass().getResourceAsStream("/images/LogoPrincipal.jpg")
                );
                stage.getIcons().add(icon);
            } catch (Exception iconError) {
                // ignorar se não carregar ícone
            }

            stage.showAndWait();
        } catch (Exception e) {
            System.err.println("Erro ao abrir tela de edição de chamado: " + e.getMessage());
            e.printStackTrace();
            AlertUtils.showError("Erro", "Não foi possível abrir a tela de edição do chamado.\n\nErro: " + e.getMessage());
        }
    }

    public void excluirChamado(Chamado chamado) {
        if (chamado == null) {
            AlertUtils.showWarning("Aviso", "Selecione um chamado para excluir.");
            return;
        }

        if (chamado.getChamadoID() == null || chamado.getChamadoID().trim().isEmpty()) {
            AlertUtils.showError(
                "Erro de Dados",
                "Nao e possivel excluir este chamado porque ele nao possui um ID valido."
            );
            return;
        }

        boolean confirmado = AlertUtils.showConfirmation(
            "Confirmar Exclusao",
            String.format(
                "Deseja realmente excluir o chamado?\n\n" +
                "ID: %s\n" +
                "Titulo: %s\n" +
                "Prioridade: %s\n" +
                "Usuario: %s\n\n" +
                "Esta acao nao pode ser desfeita!",
                chamado.getChamadoID(),
                chamado.getTitulo(),
                chamado.getPrioridade(),
                chamado.getNomeDoUsuario()
            )
        );
        
        if (!confirmado) {
            return;
        }

        boolean sucesso = chamadoService.excluirChamado(chamado);
        
        if (sucesso) {
            AlertUtils.showSuccess(
                "Sucesso",
                "Chamado excluido com sucesso!\n\n" +
                "ID: " + chamado.getChamadoID() + "\n" +
                "Titulo: " + chamado.getTitulo()
            );
            
            onDataChanged.run();
        } else {
            AlertUtils.showError(
                "Erro ao Excluir",
                "Nao foi possivel excluir o chamado.\n\n" +
                "ID do Chamado: " + chamado.getChamadoID()
            );
        }
    }
}
