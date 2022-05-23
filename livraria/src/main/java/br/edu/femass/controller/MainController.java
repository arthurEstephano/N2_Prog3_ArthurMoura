package br.edu.femass.controller;

import br.edu.femass.model.Autor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Button BtnAutores;

    @FXML
    private Button BtnGeneros;

    @FXML
    private Button BtnUsuarios;

    @FXML
    private void BtnAutores_Action(ActionEvent evento) {
        abrirTela("Autor", "Autores");
    }

    @FXML
    private void BtnGeneros_Action(ActionEvent evento) {
        abrirTela("Genero", "Gêneros");
    }

    @FXML
    private void BtnUsuarios_Action(ActionEvent evento) {
        abrirTela("Usuario", "Usuários");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void abrirTela(String nome, String titulo) {

        try {
            Parent root = null;
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/" + nome + ".fxml")));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            scene.getRoot().setStyle("-fx-font-family: 'serif'");

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
