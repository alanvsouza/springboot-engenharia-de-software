package br.unesp.rc.springtutorial.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PessoaFisicaAudit")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FisicaAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "id_pessoa")
    private Long idPessoa;

    @Column(name = "cpf", length = 14)
    private String cpf;

    @Column(name = "nome", length = 50)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", length = 10)
    private OperationType operationType;

    @Column(name = "operation_timestamp")
    private LocalDateTime operationTimestamp;
}
