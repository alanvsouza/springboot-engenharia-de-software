package br.unesp.rc.springtutorial.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import br.unesp.rc.springtutorial.entity.FisicaAudit;
import br.unesp.rc.springtutorial.entity.OperationType;
import br.unesp.rc.springtutorial.repository.FisicaAuditRepository;

@ExtendWith(MockitoExtension.class)
class FisicaReportServiceTest {

    @Mock
    private FisicaAuditRepository auditRepository;

    @InjectMocks
    private FisicaReportService service;

    @TempDir
    Path tempDir;

    private Path reportFile;

    @BeforeEach
    void setUp() {
        reportFile = tempDir.resolve("report.html");
        ReflectionTestUtils.setField(service, "reportPath", reportFile.toString());
    }

    @Test
    @DisplayName("FREP-05: creates report.html with valid HTML skeleton when file does not exist")
    void generateReport_createsHtmlFileWhenMissing() throws IOException {
        when(auditRepository.findByOperationTimestampAfter(any())).thenReturn(List.of());

        service.generateReport();

        assertTrue(Files.exists(reportFile), "report.html must be created");
        String content = Files.readString(reportFile, StandardCharsets.UTF_8);
        assertTrue(content.contains("<!DOCTYPE html>"), "must contain DOCTYPE");
        assertTrue(content.contains("<html>"), "must contain <html>");
        assertTrue(content.contains("<body>"), "must contain <body>");
        assertTrue(content.contains("</body>"), "must contain </body>");
        assertTrue(content.contains("</html>"), "must contain </html>");
    }

    @Test
    @DisplayName("FREP-06: empty window section states 'No changes in this window'")
    void generateReport_appendsNoChangesWhenEmpty() throws IOException {
        when(auditRepository.findByOperationTimestampAfter(any())).thenReturn(List.of());

        service.generateReport();

        String content = Files.readString(reportFile, StandardCharsets.UTF_8);
        assertTrue(content.contains("No changes in this window."),
            "must include 'No changes in this window.' for empty window");
    }

    @Test
    @DisplayName("FREP-04: scheduler appends section with audit entries grouped in a table")
    void generateReport_appendsSectionWithAuditEntries() throws IOException {
        FisicaAudit audit = new FisicaAudit(
            1L, 42L, "111.222.333-44", "user1", OperationType.INSERT,
            LocalDateTime.of(2026, 7, 29, 10, 2, 13)
        );
        when(auditRepository.findByOperationTimestampAfter(any())).thenReturn(List.of(audit));

        service.generateReport();

        String content = Files.readString(reportFile, StandardCharsets.UTF_8);
        assertTrue(content.contains("<section>"), "must contain <section>");
        assertTrue(content.contains("INSERT"), "must contain operation type INSERT");
        assertTrue(content.contains("111.222.333-44"), "must contain cpf");
        assertTrue(content.contains("user1"), "must contain nome");
        assertTrue(content.contains("2026-07-29 10:02:13"), "must contain formatted timestamp");
    }

    @Test
    @DisplayName("FREP-04: second generateReport() call accumulates — first section is preserved")
    void generateReport_accumulatesSectionsAcrossMultipleCalls() throws IOException {
        FisicaAudit audit = new FisicaAudit(
            1L, 42L, "111.222.333-44", "user1", OperationType.INSERT,
            LocalDateTime.of(2026, 7, 29, 10, 2, 13)
        );
        when(auditRepository.findByOperationTimestampAfter(any()))
            .thenReturn(List.of(audit))
            .thenReturn(List.of());

        service.generateReport();
        service.generateReport();

        String content = Files.readString(reportFile, StandardCharsets.UTF_8);
        // both sections present: the INSERT from tick 1 and "no changes" from tick 2
        assertTrue(content.contains("INSERT"), "first section must be preserved");
        assertTrue(content.contains("No changes in this window."), "second section must be appended");
        assertFalse(content.indexOf("<section>") == content.lastIndexOf("<section>"),
            "must have more than one <section>");
    }
}
