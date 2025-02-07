package project.mozit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.mozit.service.AzureMetricsService;

@RestController
public class AzureMetricsController {

    private final AzureMetricsService azureMetricsService;

    @Autowired
    public AzureMetricsController(AzureMetricsService azureMetricsService) {
        this.azureMetricsService = azureMetricsService;
    }

    // Azure Metric 데이터를 가져오는 엔드포인트
    @GetMapping("/api/azure-metrics")
    public ResponseEntity<String> getAzureMetrics(@RequestParam String sasToken) {
        // Azure Storage에서 데이터를 가져오는 서비스 호출
        String metricsData = azureMetricsService.fetchMetrics(sasToken);

        // 데이터를 그대로 응답으로 반환
        return ResponseEntity.ok(metricsData);
    }
}
