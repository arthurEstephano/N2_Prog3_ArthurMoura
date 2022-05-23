package br.edu.femass.dao;

import br.edu.femass.model.Autor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AutorDao extends DaoPostgres implements  Dao<Autor>{
    @Override
    public List<Autor> listar() throws Exception {
        String sql = "select * from autor order by nome";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ResultSet rs = ps.executeQuery();

        List<Autor> autores = new ArrayList<Autor>();
        while (rs.next()) {
            Autor autor = new Autor();
            autor.setNome(rs.getString("nome"));
            autor.setSobreNome(rs.getString("sobrenome"));
            autor.setId(rs.getLong("id"));
            autores.add(autor);
        }

        return autores;
    }

    @Override
    public void gravar(Autor value) throws Exception {
        String sql = "INSERT INTO autor (nome, sobrenome) VALUES (?,?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setString(1, value.getNome());
        ps.setString(2, value.getSobreNome());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong(1));

    }

    @Override
    public void alterar(Autor value) throws Exception {
        String sql = "update autor set nome = ?, sobrenome = ? where id = ?";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ps.setString(1, value.getNome());
        ps.setString(2, value.getSobreNome());
        ps.setLong(3, value.getId());
        ps.executeUpdate();
    }

    @Override
    public void excluir(Autor value) throws Exception {
        String sql = "delete from autor where id = ?";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ps.setLong(1, value.getId());
        ps.executeUpdate();
    }
}
