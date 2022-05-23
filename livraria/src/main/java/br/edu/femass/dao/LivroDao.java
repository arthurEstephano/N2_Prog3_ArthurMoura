package br.edu.femass.dao;

import br.edu.femass.model.Autor;
import br.edu.femass.model.Genero;
import br.edu.femass.model.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivroDao extends DaoPostgres implements  Dao<Livro>{
    @Override
    public List<Livro> listar() throws Exception {
        String sql = "select " +
                "livro.id as livro_id, " +
                "livro.nome as livro_nome, " +
                "livro.edicao as livro_edicao, " +
                "livro.ano as livro_ano, " +
                "genero.id as genero_id, " +
                "genero.nome as genero_nome " +
                "from livro, genero " +
                "where livro.id_genero = genero.id " +
                "order by livro_nome";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ResultSet rs = ps.executeQuery();

        List<Livro> livros = new ArrayList<Livro>();
        while (rs.next()) {
            Livro livro = new Livro();
            livro.setId(rs.getLong("livro_id"));
            livro.setNome(rs.getString("livro_nome"));
            livro.setAno(rs.getInt("livro_ano"));
            livro.setEdicao(rs.getString("livro_edicao"));

            Genero genero = new Genero();
            genero.setId(rs.getLong("genero_id"));
            genero.setNome(rs.getString("genero_nome"));
            livro.setGenero(genero);

            sql = "select " +
                    "autor.id, " +
                    "autor.nome, " +
                    "autor.sobrenome " +
                    "from autor, livro_autor " +
                    "where livro_autor.id_livro = ? and " +
                    "livro_autor.id_autor = autor.id";
            PreparedStatement ps2 = getPreparedStatement(sql, false);
            ps2.setLong(1, livro.getId());
            ResultSet rsAutor = ps2.executeQuery();

            List<Autor> autores = new ArrayList<Autor>();
            while (rsAutor.next()) {
                Autor autor = new Autor();
                autor.setId(rsAutor.getLong("id"));
                autor.setSobreNome(rsAutor.getString("sobrenome"));
                autor.setNome(rsAutor.getString("nome"));
                autores.add(autor);
            }

            livro.setAutores(autores);

            livros.add(livro);

        }

        return livros;
    }

    @Override
    public void gravar(Livro value) throws Exception {
       String sql = "INSERT INTO livro (nome, edicao, ano, id_genero) VALUES (?,?,?,?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setString(1, value.getNome());
        ps.setString(2, value.getEdicao());
        ps.setInt(3, value.getAno());
        ps.setLong(4, value.getGenero().getId());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong(1));

        for (Autor autor: value.getAutores()) {
            sql = "insert into livro_autor (id_livro, id_autor) values (?,?)";
            PreparedStatement ps2 = getPreparedStatement(sql, false);
            ps2.setLong(1, value.getId());
            ps2.setLong(2, autor.getId());

            ps2.executeUpdate();

        }

    }

    @Override
    public void alterar(Livro value) throws Exception {
        Connection conexao = getConexao();
        conexao.setAutoCommit(false);
        try {
            String sql = "UPDATE livro " +
                    "SET nome = ?, " +
                    "edicao = ?, " +
                    "ano = ?, " +
                    "id_genero = ? " +
                    "WHERE id = ?";
            PreparedStatement ps = getPreparedStatement(sql, true);
            ps.setString(1, value.getNome());
            ps.setString(2, value.getEdicao());
            ps.setInt(3, value.getAno());
            ps.setLong(4, value.getGenero().getId());
            ps.setLong(5, value.getId());

            ps.executeUpdate();

            for (Autor autor : value.getAutores()) {
                sql = "UPDATE livro_autor " +
                        "SET  id_autor = ? " +
                        "WHERE id_livro = ?";
                PreparedStatement ps2 = getPreparedStatement(sql, false);
                ps2.setLong(1, autor.getId());
                ps2.setLong(2, value.getId());
                ps2.executeUpdate();
                conexao.commit();
            }
        }catch (SQLException exception) {
            conexao.rollback();
            throw exception;
        }
    }

    @Override
    public void excluir(Livro value) throws Exception {
        Connection conexao = getConexao();
        conexao.setAutoCommit(false);
        try {
            String sql = "delete from livro_autor where id_livro = ?";
            PreparedStatement ps = getPreparedStatement(sql, false);
            ps.setLong(1, value.getId());
            ps.executeUpdate();

            sql = "delete from livro where id = ?";
            PreparedStatement ps2 = getPreparedStatement(sql, false);
            ps2.setLong(1, value.getId());
            ps2.executeUpdate();
            conexao.commit();
        }catch (SQLException exception){
            conexao.rollback();
            throw exception;
        }
    }
}
