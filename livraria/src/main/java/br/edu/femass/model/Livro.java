package br.edu.femass.model;

import lombok.Data;

import java.util.List;

@Data
public class Livro {
    private Long id;
    private String nome;
    private String edicao;
    private Integer ano;
    private List<Autor> autores;
    private Genero genero;

    @Override
    public String toString() {
        return this.nome;
    }
}
