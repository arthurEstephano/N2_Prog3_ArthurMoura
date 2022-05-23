package br.edu.femass.dao;

import br.edu.femass.model.Aluno;
import br.edu.femass.model.Professor;
import br.edu.femass.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao extends DaoPostgres implements  Dao<Usuario>{
    @Override
    public List<Usuario> listar() throws Exception {
        String sql = "select " +
                "usuario.id as id, " +
                "usuario.nome as nome, " +
                "usuario.prazoDevolucao as prazodevolucao, " +
                "aluno.matricula as matricula " +
                "from usuario inner join aluno on usuario.id = aluno.id_usuario  " +
                "order by usuario.nome asc";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ResultSet rs = ps.executeQuery();

        List<Usuario> usuarios = new ArrayList<Usuario>();

        while (rs.next()) {
            Aluno aluno = new Aluno();
            aluno.setNome(rs.getString("nome"));
            aluno.setId(rs.getLong("id"));
            aluno.setMatricula(rs.getString("matricula"));
            aluno.setPrazoDevolucao(rs.getInt("prazodevolucao"));
            usuarios.add(aluno);
        }

        sql = "select " +
                "usuario.id as id, " +
                "usuario.nome as nome, " +
                "usuario.prazoDevolucao as prazodevolucao, " +
                "professor.formacao as formacao " +
                "from usuario inner join professor on usuario.id = professor.id_usuario  " +
                "order by usuario.nome";
        ps = getPreparedStatement(sql, false);
        rs = ps.executeQuery();

        while (rs.next()) {
            Professor professor = new Professor();
            professor.setNome(rs.getString("nome"));
            professor.setId(rs.getLong("id"));
            professor.setFormacao(rs.getString("formacao"));
            professor.setPrazoDevolucao(rs.getInt("prazodevolucao"));
            usuarios.add(professor);
        }

        return usuarios;
    }

    @Override
    public void gravar(Usuario value) throws Exception {
        String sql = "INSERT INTO usuario (nome, prazodevolucao) VALUES (?,?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setString(1, value.getNome());
        ps.setInt(2, value.getPrazoDevolucao());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong(1));

        if (value instanceof  Professor) {
            sql = "INSERT INTO professor (id_usuario, formacao) VALUES (?,?)";
            ps = getPreparedStatement(sql, true);
            ps.setString(2, ((Professor) value).getFormacao());
        } else {
            sql = "INSERT INTO aluno (id_usuario ,matricula) VALUES (?,?)";
            ps = getPreparedStatement(sql, true);
            ps.setString(2, ((Aluno) value).getMatricula());
        }
        ps.setLong(1, value.getId());
        ps.executeUpdate();

    }

    @Override
    public void alterar(Usuario value) throws Exception {
        String sql = "update usuario set nome = ?, prazodevolucao = ? where id = ?";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ps.setString(1, value.getNome());
        ps.setInt(2, value.getPrazoDevolucao());
        ps.setLong(3, value.getId());
        ps.executeUpdate();

        if (value instanceof Professor) {
            sql = "update professor set formacao = ? where id_usuario = ?";
            ps = getPreparedStatement(sql, true);
            ps.setString(1, ((Professor) value).getFormacao());
        } else {
            sql = "update aluno set matricula = ? where id_usuario = ?";
            ps = getPreparedStatement(sql, true);
            ps.setString(1, ((Aluno) value).getMatricula());
        }
        ps.setLong(1, value.getId());
        ps.executeUpdate();

    }

    @Override
    public void excluir(Usuario value) throws Exception {
        Connection conexao = getConexao();
        conexao.setAutoCommit(false);
        try {
            String sql = "";
            if (value instanceof Professor) {
                sql = "delete from professor where id_usuario = ?";
            } else {
                sql = "delete from aluno where id_usuario = ?";
            }
            PreparedStatement ps1 = conexao.prepareStatement(sql);
            ps1.setLong(1, value.getId());
            ps1.executeUpdate();

            sql = "delete from usuario where id = ?";
            PreparedStatement ps2 = conexao.prepareStatement(sql);

            ps2.setLong(1, value.getId());
            ps2.executeUpdate();
            conexao.commit();
        } catch (SQLException exception) {
            conexao.rollback();
            throw exception;
        }
    }
}
