package br.edu.femass.controller;

import br.edu.femass.dao.GeneroDao;
import br.edu.femass.model.Autor;
import br.edu.femass.model.Genero;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class GeneroController implements Initializable {
    private final GeneroDao generoDao = new GeneroDao();

    @FXML
    private ListView<Genero> LstGeneros;

    @FXML
    private Button BtnIncluir;

    @FXML
    private Button BtnExcluir;

    @FXML
    private Button BtnAlterar;
    @FXML
    private Button BtnGravar;

    @FXML
    private Button BtnCancelar;

    @FXML
    private TextField TxtNome;



    private void limparTela() {
        TxtNome.setText("");
    }
    private void habilitarInterface(Boolean incluir) {
        TxtNome.setDisable(!incluir);
        BtnGravar.setDisable(!incluir);
        BtnCancelar.setDisable(!incluir);
        BtnExcluir.setDisable(incluir);
        BtnIncluir.setDisable(incluir);
        LstGeneros.setDisable(incluir);
    }

    private void exibirGenero() {
        Genero genero = LstGeneros.getSelectionModel().getSelectedItem();
        if (genero==null) return;
        TxtNome.setText(genero.getNome());
    }

    @FXML
    private void LstGeneros_MouseClicked(MouseEvent evento) {
        exibirGenero();
    }

    @FXML
    private void LstGeneros_KeyPressed(KeyEvent evento) {
        exibirGenero();
    }

    @FXML
    private void BtnIncluir_Action(ActionEvent evento) {
        atualizarLista();
        habilitarInterface(true);
        limparTela();
        TxtNome.requestFocus();
    }

    @FXML
    private void BtnExcluir_Action(ActionEvent evento) {
        Genero genero = LstGeneros.getSelectionModel().getSelectedItem();

        if (genero==null) return;

        try {
            generoDao.excluir(genero);
        } catch (Exception e) {
            e.printStackTrace();
        }

        atualizarLista();

    }

    @FXML
    private void BtnAlterar_Action(ActionEvent evento) {
        Genero genero = LstGeneros.getSelectionModel().getSelectedItem();

        if (genero==null) return;
        habilitarInterface(true);
        BtnGravar.setText("Alterar");
        TxtNome.requestFocus();

    }
    @FXML
    private void BtnGravar_Action(ActionEvent evento) {
        Genero genero = new Genero();
        genero.setNome(TxtNome.getText());
        if (TxtNome.getText().equals("")){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.show();
        }
        else {
            if (Objects.equals(BtnGravar.getText(), "Gravar")) {
                try {
                    generoDao.gravar(genero);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                genero.setId(LstGeneros.getSelectionModel().getSelectedItem().getId());
                try {
                    generoDao.alterar(genero);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            atualizarLista();
            habilitarInterface(false);
        }
    }
    @FXML
    private void BtnCancelar_Action(ActionEvent evento) {
        habilitarInterface(false);
    }


    private void atualizarLista() {
        List<Genero> generos;
        try {
            generos = generoDao.listar();
        } catch (Exception e) {
            generos = new ArrayList<>();
        }
        ObservableList<Genero> autoresOb = FXCollections.observableArrayList(generos);
        LstGeneros.setItems(autoresOb);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        atualizarLista();
    }
}
