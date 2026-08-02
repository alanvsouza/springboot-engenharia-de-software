package br.unesp.rc.springtutorial.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.unesp.rc.springtutorial.entity.FisicaAudit;

public interface FisicaAuditRepository extends JpaRepository<FisicaAudit, Long> {
    List<FisicaAudit> findByOperationTimestampAfter(LocalDateTime since);
}
