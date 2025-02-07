package project.mozit.controller;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.specialized.BlockBlobClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    // Azure Blob Storage에 파일을 업로드하는 메서드
    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = uploadToAzureBlob(file);
        return ResponseEntity.ok(fileUrl);
    }

    // Azure Blob Storage에 파일을 업로드하는 실제 로직
    private String uploadToAzureBlob(MultipartFile file) {
        try {
            String encodedSasToken = encodeSasToken(sasToken);

            BlobServiceClient blobServiceClient = createBlobServiceClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            String blobName = "question-images/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            BlockBlobClient blobClient = containerClient.getBlobClient(blobName).getBlockBlobClient();

            try (InputStream fileInputStream = file.getInputStream()) {
                blobClient.upload(fileInputStream, file.getSize(), true);
            }

            return blobClient.getBlobUrl();
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 IOException 발생", e);
        }
    }

    private String encodeSasToken(String sasToken) {
        try {
            return URLEncoder.encode(sasToken, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("SAS 토큰 인코딩 오류", e);
        }
    }

    private BlobServiceClient createBlobServiceClient() {
        return new BlobServiceClientBuilder()
                .endpoint("https://" + storageAccountName + ".blob.core.windows.net")
                .sasToken(sasToken)
                .buildClient();
    }
}
