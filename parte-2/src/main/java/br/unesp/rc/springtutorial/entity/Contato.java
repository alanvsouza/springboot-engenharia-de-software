package br.unesp.rc.springtutorial.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "Contato")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Contato implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idContato;

    private String telefoneResidencial;
    private String telefoneComercial;
    private String celular;
    private String email;

    public Contato() {
    }
}
