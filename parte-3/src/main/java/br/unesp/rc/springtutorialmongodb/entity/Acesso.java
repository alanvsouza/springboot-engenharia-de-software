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
public class Acesso implements Serializable {
    private static final long serialVersionUID = 1L;

    private String usuario;

    private String senha;

    public Acesso() {   
    }
}
