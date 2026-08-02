package br.unesp.rc.springtutorialmongodb.entity;

import org.springframework.data.annotation.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true, includeFieldNames = true)
public class Juridica extends Pessoa {
    private static final long serialVersionUID = 1L;

    @Id
    private String cnpj;

    public Juridica() {
    }
}
