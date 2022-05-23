package br.edu.femass.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


public class Aluno extends Usuario{
    @Getter
    @Setter
    private String matricula;

    public Aluno() {
        this.prazoDevolucao = 5;
    }
}
