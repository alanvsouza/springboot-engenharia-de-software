package br.unesp.rc.springtutorialmongodb.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, exclude = {"dataNascimento"})
@ToString(callSuper = true, includeFieldNames = true)
public class Fisica extends Pessoa {
    private static final long serialVersionUID = 1L;

    @Id
    private String cpf;
    private Date dataNascimento;

    public Fisica() {
    }
}
