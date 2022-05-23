package br.edu.femass.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


public class Professor extends Usuario{
    @Getter
    @Setter
    private String formacao;

    public Professor() {
        this.prazoDevolucao = 30;
    }
}
