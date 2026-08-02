package br.unesp.rc.springtutorial.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.unesp.rc.springtutorial.entity.Fisica;
import br.unesp.rc.springtutorial.entity.FisicaAudit;
import br.unesp.rc.springtutorial.entity.OperationType;
import br.unesp.rc.springtutorial.repository.FisicaAuditRepository;
import br.unesp.rc.springtutorial.repository.FisicaRepository;

@ExtendWith(MockitoExtension.class)
class FisicaServiceAuditTest {

    @Mock
    private FisicaRepository repository;

    @Mock
    private FisicaAuditRepository auditRepository;

    @InjectMocks
    private FisicaService service;

    private Fisica fisica;

    @BeforeEach
    void setUp() {
        fisica = new Fisica();
        fisica.setNome("user1");
        fisica.setCpf("111.222.333-44");
    }

    @Test
    @DisplayName("FREP-01: save() writes INSERT audit entry with cpf, nome, and timestamp")
    void save_writesInsertAudit() {
        when(repository.save(fisica)).thenReturn(fisica);

        service.save(fisica);

        ArgumentCaptor<FisicaAudit> captor = ArgumentCaptor.forClass(FisicaAudit.class);
        verify(auditRepository, times(1)).save(captor.capture());

        FisicaAudit audit = captor.getValue();
        assertEquals(OperationType.INSERT, audit.getOperationType());
        assertEquals("111.222.333-44", audit.getCpf());
        assertEquals("user1", audit.getNome());
        assertNotNull(audit.getOperationTimestamp());
    }

    @Test
    @DisplayName("FREP-02: update() writes UPDATE audit entry with cpf, nome, and timestamp")
    void update_writesUpdateAudit() {
        when(repository.save(fisica)).thenReturn(fisica);

        service.update(fisica);

        ArgumentCaptor<FisicaAudit> captor = ArgumentCaptor.forClass(FisicaAudit.class);
        verify(auditRepository, times(1)).save(captor.capture());

        FisicaAudit audit = captor.getValue();
        assertEquals(OperationType.UPDATE, audit.getOperationType());
        assertEquals("111.222.333-44", audit.getCpf());
        assertEquals("user1", audit.getNome());
        assertNotNull(audit.getOperationTimestamp());
    }

    @Test
    @DisplayName("FREP-03: delete() writes DELETE audit entry before removing the row")
    void delete_writesDeleteAuditBeforeRemoval() {
        service.delete(fisica);

        ArgumentCaptor<FisicaAudit> captor = ArgumentCaptor.forClass(FisicaAudit.class);

        // audit must be saved before repository.delete is called
        var inOrder = org.mockito.Mockito.inOrder(auditRepository, repository);
        inOrder.verify(auditRepository).save(captor.capture());
        inOrder.verify(repository).delete(fisica);

        FisicaAudit audit = captor.getValue();
        assertEquals(OperationType.DELETE, audit.getOperationType());
        assertEquals("111.222.333-44", audit.getCpf());
        assertEquals("user1", audit.getNome());
        assertNotNull(audit.getOperationTimestamp());
    }

    @Test
    @DisplayName("FREP-07: audit is NOT written when repository.save() throws (shared transaction rollback)")
    void save_auditNotWrittenWhenRepositoryThrows() {
        when(repository.save(fisica)).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> service.save(fisica));

        verify(auditRepository, never()).save(any());
    }

    @Test
    @DisplayName("Edge case: two save() calls for same CPF produce two separate INSERT audit entries (no dedup)")
    void save_twiceForSameCpf_producesTwoAuditEntries() {
        Fisica fisica2 = new Fisica();
        fisica2.setNome("user1-updated");
        fisica2.setCpf("111.222.333-44");

        when(repository.save(fisica)).thenReturn(fisica);
        when(repository.save(fisica2)).thenReturn(fisica2);

        service.save(fisica);
        service.save(fisica2);

        ArgumentCaptor<FisicaAudit> captor = ArgumentCaptor.forClass(FisicaAudit.class);
        verify(auditRepository, times(2)).save(captor.capture());

        List<FisicaAudit> audits = captor.getAllValues();
        assertEquals(2, audits.size(), "both operations must produce separate audit entries");
        assertEquals(OperationType.INSERT, audits.get(0).getOperationType());
        assertEquals(OperationType.INSERT, audits.get(1).getOperationType());
        assertEquals("111.222.333-44", audits.get(0).getCpf());
        assertEquals("111.222.333-44", audits.get(1).getCpf());
    }
}
