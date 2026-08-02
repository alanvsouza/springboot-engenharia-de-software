package br.unesp.rc.springtutorial.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Acesso implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "acesso_usuario", unique = true)
    private String usuario;

    @Column(name = "acesso_senha")
    private String senha;

    public Acesso() {   
    }
}
