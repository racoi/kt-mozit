package project.mozit.controller;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.specialized.BlockBlobClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
        try {
            String fileUrl = uploadToAzureBlob(file);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("알 수 없는 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // Azure Blob Storage에 파일을 업로드하는 실제 로직
    private String uploadToAzureBlob(MultipartFile file) throws IOException {
        // SAS 토큰 URL 인코딩
        String encodedSasToken = encodeSasToken(sasToken);

        // BlobServiceClient 생성
        BlobServiceClient blobServiceClient = createBlobServiceClient(encodedSasToken);
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

        // Blob 이름 생성
        String blobName = "question-images/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        BlockBlobClient blobClient = containerClient.getBlobClient(blobName).getBlockBlobClient();

        try (InputStream fileInputStream = file.getInputStream()) {
            // 파일 업로드
            blobClient.upload(fileInputStream, file.getSize(), true);
        }

        return blobClient.getBlobUrl();
    }

    // SAS 토큰을 URL 인코딩하는 메서드
    private String encodeSasToken(String sasToken) {
        return URLEncoder.encode(sasToken, StandardCharsets.UTF_8);
    }

    // BlobServiceClient를 생성하는 메서드
    private BlobServiceClient createBlobServiceClient(String encodedSasToken) {
        return new BlobServiceClientBuilder()
                .endpoint("https://" + storageAccountName + ".blob.core.windows.net?" + encodedSasToken)
                .buildClient();
    }
}
