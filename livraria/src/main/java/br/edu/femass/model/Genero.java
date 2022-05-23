package br.edu.femass.model;

import lombok.Data;

@Data
public class Genero {
    private Long id;
    private String nome;

    public String toString() {
        return this.nome;
    }
}
