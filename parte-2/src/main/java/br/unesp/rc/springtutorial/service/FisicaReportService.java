package br.unesp.rc.springtutorial.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.unesp.rc.springtutorial.entity.FisicaAudit;
import br.unesp.rc.springtutorial.repository.FisicaAuditRepository;

@Component
public class FisicaReportService {

    private static final Logger log = LoggerFactory.getLogger(FisicaReportService.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int WINDOW_MINUTES = 5;

    @Autowired
    private FisicaAuditRepository auditRepository;

    @Value("${fisica.report.path:src/main/resources/static/report.html}")
    private String reportPath;

    @Scheduled(fixedRate = 300_000)
    public void generateReport() {
        LocalDateTime since = LocalDateTime.now().minusMinutes(WINDOW_MINUTES);
        List<FisicaAudit> entries;
        try {
            entries = auditRepository.findByOperationTimestampAfter(since);
        } catch (Exception e) {
            log.error("Failed to query audit table for report generation", e);
            return;
        }

        String section = buildSection(entries);

        try {
            Path path = Path.of(reportPath);
            Files.createDirectories(path.getParent());

            String content;
            if (Files.exists(path)) {
                content = Files.readString(path, StandardCharsets.UTF_8);
            } else {
                content = htmlSkeleton();
            }

            String updated = content.replace("</body>", section + "\n</body>");
            Files.writeString(path, updated, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Failed to write report.html", e);
        }
    }

    private String buildSection(List<FisicaAudit> entries) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        StringBuilder sb = new StringBuilder();
        sb.append("  <section>\n");
        sb.append("    <h2>Report — ").append(timestamp).append("</h2>\n");

        if (entries.isEmpty()) {
            sb.append("    <p>No changes in this window.</p>\n");
        } else {
            sb.append("    <table>\n");
            sb.append("      <tr><th>Operation</th><th>CPF</th><th>Nome</th><th>Timestamp</th></tr>\n");
            for (FisicaAudit a : entries) {
                sb.append("      <tr><td>").append(a.getOperationType())
                  .append("</td><td>").append(a.getCpf())
                  .append("</td><td>").append(a.getNome())
                  .append("</td><td>").append(a.getOperationTimestamp().format(FORMATTER))
                  .append("</td></tr>\n");
            }
            sb.append("    </table>\n");
        }

        sb.append("  </section>");
        return sb.toString();
    }

    private String htmlSkeleton() {
        return """
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Fisica Change Report</title>
  <style>table{border-collapse:collapse}th,td{border:1px solid #ccc;padding:4px 8px}</style>
</head>
<body>
</body>
</html>""";
    }
}
