package br.unesp.rc.springtutorial.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.unesp.rc.springtutorial.entity.Fisica;
import br.unesp.rc.springtutorial.entity.FisicaAudit;
import br.unesp.rc.springtutorial.entity.OperationType;
import br.unesp.rc.springtutorial.repository.FisicaAuditRepository;
import br.unesp.rc.springtutorial.repository.FisicaRepository;

@Component
public class FisicaService {
    @Autowired
    private FisicaRepository repository;

    @Autowired
    private FisicaAuditRepository auditRepository;

    public FisicaService() {
    }

    @Transactional
    public Fisica save(Fisica entity) {
        Fisica persistedEntity = null;

        if (repository != null) {
            persistedEntity = repository.save(entity);
            writeAudit(entity, OperationType.INSERT);
        }

        return persistedEntity;
    }

    @Cacheable(value = "fisica", key = "#cpf")
    public Fisica findByCpf(String cpf) {
        Fisica insertedEntity = null;

        if (repository != null) {
            insertedEntity = repository.findByCpf(cpf);
        }

        return insertedEntity;
    }

    @Transactional
    @CacheEvict(value = "fisica", key = "#entity.cpf")
    public void delete(Fisica entity) {
        if (repository != null) {
            writeAudit(entity, OperationType.DELETE);
            repository.delete(entity);
        }
    }

    @Transactional
    @CacheEvict(value = "fisica", key = "#entity.cpf")
    public Fisica update(Fisica entity) {
        Fisica persistedEntity = null;

        if (repository != null) {
            persistedEntity = repository.save(entity);
            writeAudit(entity, OperationType.UPDATE);
        }

        return persistedEntity;
    }

    public List<Fisica> findAll() {
        List<Fisica> list = null;

        if (repository != null) {
            list = new ArrayList<>();
            list = repository.findAll();
        }

        return list;
    }

    private void writeAudit(Fisica entity, OperationType operationType) {
        FisicaAudit audit = new FisicaAudit(
            null,
            entity.getIdPessoa(),
            entity.getCpf(),
            entity.getNome(),
            operationType,
            LocalDateTime.now()
        );
        auditRepository.save(audit);
    }
}
