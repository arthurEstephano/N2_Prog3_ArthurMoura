package br.edu.femass.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public abstract class Usuario {
    protected Long id;
    protected String nome;
    protected Integer prazoDevolucao;


    public String toString() {
        return this.nome;
    }
}
