package project.mozit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AzureMetricsService {

    @Value("${azure.storage.url}")
    private String storageUrl;

    private final RestTemplate restTemplate;

    public AzureMetricsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Azure Storage에서 데이터를 가져오는 메서드
    public String fetchMetrics(String sasToken) {
        // URL에 SAS 토큰 추가
        String url = storageUrl + "?" + sasToken;

        try {
            // 데이터를 가져옴
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getBody(); // 데이터를 그대로 반환
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching Azure metrics", e);
        }
    }
}
