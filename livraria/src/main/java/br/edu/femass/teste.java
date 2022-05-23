package br.edu.femass;

import br.edu.femass.dao.AutorDao;
import br.edu.femass.dao.GeneroDao;
import br.edu.femass.dao.LivroDao;
import br.edu.femass.dao.UsuarioDao;
import br.edu.femass.model.*;

import java.util.List;

public class teste {

    public static void main(String[] args) {
        try {
            List<Livro> livros = new LivroDao().listar();

            for (Livro l: livros) {
                System.out.println("Nome: " + l.getNome());
                System.out.println("ID: " + l.getId());
                System.out.println("Edição: " + l.getEdicao());
                System.out.println("Ano: " + l.getAno());
                System.out.println("Gênero: " + l.getGenero().getId().toString() + " - " + l.getGenero().getNome());
                for (Autor a: l.getAutores()) {
                    System.out.println("ID: " + a.getId());
                    System.out.println("Nome: " + a.getNome());
                    System.out.println("Sobrenome: " + a.getSobreNome());
                }
                System.out.println("===========================");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void gravarLivro() {
        try {
            List<Autor> autores = new AutorDao().listar();

            List<Genero> generos = new GeneroDao().listar();

            Livro l = new Livro();
            l.setAutores(autores);
            l.setGenero(generos.get(1));
            l.setNome("Tanembaum para Leigos");
            l.setEdicao("vigésima quarta");
            l.setAno(2024);

            new LivroDao().gravar(l);
            System.out.println("LIvro gravado com o código " + l.getId().toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
