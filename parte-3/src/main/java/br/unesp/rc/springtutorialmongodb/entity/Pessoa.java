package br.unesp.rc.springtutorialmongodb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    private long idPessoa;
    private String nome;
    private List<Endereco> endereco;
    private Acesso acesso;
    private Contato contato;

    public Pessoa() {
        this.endereco = new ArrayList<>();
    }

    public void setEndereco(Endereco endereco) {
        this.endereco.add(endereco);
    }
}
