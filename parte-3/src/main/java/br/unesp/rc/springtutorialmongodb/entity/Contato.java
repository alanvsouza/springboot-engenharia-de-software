package br.unesp.rc.springtutorialmongodb.entity;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Contato implements Serializable {
    private static final long serialVersionUID = 1L;

    private long idContato;

    private String telefoneResidencial;
    private String telefoneComercial;
    private String celular;
    private String email;

    public Contato() {
    }
}
