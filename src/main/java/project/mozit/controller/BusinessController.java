package project.mozit.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/business")
public class BusinessController {

    @Value("${business.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/verify")
    public ResponseEntity<String> verifyBusinessNumber(@RequestParam String businessNumber) {
        String apiUrl = UriComponentsBuilder.fromHttpUrl("https://bizno.net/api/fapi")
                .queryParam("key", apiKey)
                .queryParam("status", "Y")
                .queryParam("q", businessNumber)
                .queryParam("type", "json")
                .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        return ResponseEntity.ok(response.getBody());
    }
}
