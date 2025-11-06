package br.com.suptec.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

/**
 * Controller do menu principal. Atualmente o menu é composto por componentes
 * (Header, MenuGrid, etc.) que possuem seus próprios controllers. Esta classe
 * atua como ponto de extensão caso seja necessário centralizar lógica do menu.
 */
public class MainMenuController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Componentes gerenciam suas próprias ações
    }
}
