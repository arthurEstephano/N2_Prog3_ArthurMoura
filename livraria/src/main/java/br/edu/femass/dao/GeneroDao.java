package br.edu.femass.dao;

import br.edu.femass.model.Genero;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GeneroDao extends DaoPostgres implements  Dao<Genero>{
    @Override
    public List<Genero> listar() throws Exception {
        String sql = "select * from genero order by nome";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ResultSet rs = ps.executeQuery();

        List<Genero> generos = new ArrayList<Genero>();
        while (rs.next()) {
            Genero genero = new Genero();
            genero.setNome(rs.getString("nome"));
            genero.setId(rs.getLong("id"));
            generos.add(genero);
        }

        return generos;
    }

    @Override
    public void gravar(Genero value) throws Exception {
        String sql = "INSERT INTO genero (nome) VALUES (?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setString(1, value.getNome());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong(1));

    }

    @Override
    public void alterar(Genero value) throws Exception {
        String sql = "update genero set nome = ? where id = ?";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ps.setString(1, value.getNome());
        ps.setLong(2, value.getId());
        ps.executeUpdate();

    }

    @Override
    public void excluir(Genero value) throws Exception {
        String sql = "delete from genero where id = ?";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ps.setLong(1, value.getId());
        ps.executeUpdate();
    }
}
