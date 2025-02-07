package project.mozit.controller;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.specialized.BlockBlobClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class AzureUploadController {

    @Value("${spring.cloud.azure.storage.blob.account-name}")
    private String storageAccountName;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String containerName;

    @Value("${spring.cloud.azure.storage.blob.sas-token}")
    private String sasToken;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    public ResponseEntity<String> uploadFileToAzure(@RequestParam("file") MultipartFile file) {
        try {
            // 파일을 Blob Storage에 업로드하기 위한 URL 생성
            String blobUrl = generateBlobUrl(file.getOriginalFilename());

            // Azure Blob Storage에 파일 업로드
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
            headers.set("x-ms-blob-type", "BlockBlob");

            // PUT 메서드로 파일 업로드
            ResponseEntity<String> response = restTemplate.exchange(
                    blobUrl,
                    HttpMethod.PUT,
                    new org.springframework.http.HttpEntity<>(file.getBytes(), headers),
                    String.class);

            return ResponseEntity.ok("File uploaded successfully");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading file to Azure: " + e.getMessage());
        }
    }

    private String generateBlobUrl(String fileName) throws UnsupportedEncodingException {
        String encodedSasToken = URLEncoder.encode(sasToken, "UTF-8");

        // Azure Blob Storage URL 생성
        return String.format("https://%s.blob.core.windows.net/%s/%s?%s",
                storageAccountName, containerName, "question-images/" + fileName, encodedSasToken);
    }
}
