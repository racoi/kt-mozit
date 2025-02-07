package project.mozit.controller;

import com.azure.core.util.serializer.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/azure")
public class AzureMetricsController {

    @Value("${azure.storage.api-sas-token}")
    private String sasToken;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/metrics")
    public ResponseEntity<?> getAzureMetrics() {
        System.out.println("SAS Token: " + sasToken);

        String storageUrl = "https://mozitstorage.blob.core.windows.net/insights-metrics-pt1m/resourceId=/SUBSCRIPTIONS/0A938E62-00BA-4C73-A908-3B285014B302/RESOURCEGROUPS/MOZIT/PROVIDERS/MICROSOFT.DBFORMYSQL/FLEXIBLESERVERS/MOZIT-DB/y=2025/m=02/d=05/h=06/m=00/PT1H.json";

        try {
            ResponseEntity<String> response = restTemplate.exchange(storageUrl + "?" + sasToken, HttpMethod.GET, null, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // HTTP 오류 처리
            return ResponseEntity.status(e.getStatusCode()).body("Error fetching metrics: " + e.getMessage());
        } catch (Exception e) {
            // 다른 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching metrics: " + e.getMessage());
        }
    }
}
