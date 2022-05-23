package br.edu.femass.controller;

import br.edu.femass.dao.UsuarioDao;
import br.edu.femass.model.Aluno;
import br.edu.femass.model.Genero;
import br.edu.femass.model.Professor;
import br.edu.femass.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class UsuarioController implements Initializable {
    private final UsuarioDao usuarioDao = new UsuarioDao();

    @FXML
    private ListView<Usuario> LstUsuarios;

    @FXML
    private Button BtnIncluir;

    @FXML
    private Button BtnExcluir;

    @FXML
    private Button BtnGravar;

    @FXML
    private Button BtnAlterar;
    @FXML
    private Button BtnCancelar;

    @FXML
    private TextField TxtNome;

    @FXML
    private TextField TxtPrazoDevolucao;

    @FXML
    private TextField TxtSubClasse;

    @FXML
    private Label LblSubClasse;

    @FXML
    private RadioButton Radio_Aluno;

    @FXML
    private RadioButton Radio_Professor;

    private void limparTela() {
        TxtPrazoDevolucao.setText("");
        TxtNome.setText("");
        TxtSubClasse.setText("");
    }
    private void habilitarInterface(Boolean incluir) {
        TxtNome.setDisable(!incluir);
        TxtSubClasse.setDisable(!incluir);
        Radio_Aluno.setDisable(!incluir);
        Radio_Professor.setDisable(!incluir);
        BtnGravar.setDisable(!incluir);
        BtnCancelar.setDisable(!incluir);
        BtnExcluir.setDisable(incluir);
        BtnIncluir.setDisable(incluir);
        LstUsuarios.setDisable(incluir);
    }

    private void exibirUsuario() {
        Usuario usuario = LstUsuarios.getSelectionModel().getSelectedItem();
        if (usuario==null) return;
        TxtNome.setText(usuario.getNome());
        TxtPrazoDevolucao.setText(usuario.getPrazoDevolucao().toString());
        if (usuario instanceof Professor) {
            Professor prof = (Professor) usuario;
            TxtSubClasse.setText(prof.getFormacao());
            LblSubClasse.setText("Formação");
            Radio_Aluno.setSelected(false);
            Radio_Professor.setSelected(true);
        } else {
            Aluno aluno = (Aluno) usuario;
            TxtSubClasse.setText(aluno.getMatricula());
            LblSubClasse.setText("Matrícula");
            Radio_Aluno.setSelected(true);
            Radio_Professor.setSelected(false);
        }
    }

    @FXML
    private void LstUsuarios_MouseClicked(MouseEvent evento) {
        exibirUsuario();
    }

    @FXML
    private void LstUsuarios_KeyPressed(KeyEvent evento) {
        exibirUsuario();
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
        Usuario usuario = LstUsuarios.getSelectionModel().getSelectedItem();

        if (usuario==null) return;

        try {
            usuarioDao.excluir(usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }

        atualizarLista();

    }

    @FXML
    private void BtnAlterar_Action(ActionEvent evento){
        Usuario usuario = LstUsuarios.getSelectionModel().getSelectedItem();

        if (usuario==null) return;
        habilitarInterface(true);
        Radio_Aluno.setDisable(false);
        Radio_Professor.setDisable(false);
        BtnGravar.setText("Alterar");
        TxtNome.requestFocus();

    }
    @FXML
    private void BtnGravar_Action(ActionEvent evento) throws Exception {
        Usuario usuario = LstUsuarios.getSelectionModel().getSelectedItem();

        if (usuario==null) return;

        if (Objects.equals(BtnGravar.getText(), "Gravar")) {
            try {
                if (Radio_Aluno.isSelected()) {
                    Aluno aluno = new Aluno();
                    aluno.setNome(TxtNome.getText());
                    aluno.setMatricula(TxtSubClasse.getText());
                    usuarioDao.gravar(aluno);
                } else {
                    Professor professor = new Professor();
                    professor.setNome(TxtNome.getText());
                    professor.setFormacao(TxtSubClasse.getText());
                    usuarioDao.gravar(professor);

                }
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setContentText(e.getMessage());
                errorAlert.show();
                return;
            }
        }
        else {
            try {
                if (usuario instanceof Professor) {
                    usuario.setNome(TxtNome.getText());
                    ((Professor) usuario).setFormacao(TxtSubClasse.getText());
                    usuarioDao.alterar(usuario);
                } else {
                    usuario.setNome(TxtNome.getText());
                    ((Aluno) usuario).setMatricula(TxtSubClasse.getText());
                    usuarioDao.alterar(usuario);
                }
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setContentText(e.getMessage());
                errorAlert.show();
                return;
            }

        }

        atualizarLista();
        habilitarInterface(false);
    }

    @FXML
    private void BtnCancelar_Action(ActionEvent evento) {
        habilitarInterface(false);
    }

    @FXML
    private void Radio_Aluno_KeyPressed(KeyEvent event) {
        atualizarSubClasse();
    }

    @FXML
    private void Radio_Aluno_MouseClicked(MouseEvent event) {
        atualizarSubClasse();
    }

    private void atualizarSubClasse() {
        if (Radio_Aluno.isSelected()) {
            LblSubClasse.setText("Matrícula");
        } else {
            LblSubClasse.setText("Formação");
        }
        TxtSubClasse.setText("");
    }


    private void atualizarLista() {
        List<Usuario> usuarios;
        try {
            usuarios = usuarioDao.listar();
        } catch (Exception e) {
            usuarios = new ArrayList<>();
        }
        ObservableList<Usuario> usuariosOb = FXCollections.observableArrayList(usuarios);
        LstUsuarios.setItems(usuariosOb);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        atualizarLista();
    }
}
