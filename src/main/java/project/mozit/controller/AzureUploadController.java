package project.mozit.controller;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/upload")
public class AzureUploadController {

    @Value("${spring.cloud.azure.storage.blob.account-name}")
    private String storageAccountName;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String containerName;

    @Value("${spring.cloud.azure.storage.blob.sas-token}")
    private String sasToken;

    // 파일 업로드
    @PutMapping
    public ResponseEntity<String> uploadFileToAzure(@RequestParam("file") MultipartFile file) {
        try {
            // BlobServiceClient를 사용하여 Azure Storage에 연결
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .endpoint("https://" + storageAccountName + ".blob.core.windows.net")
                    .credential(new StorageSharedKeyCredential(storageAccountName, sasToken))
                    .buildClient();

            // BlobContainerClient를 통해 컨테이너를 선택
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            // 파일 이름을 URL 인코딩
            String encodedFileName = URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8.toString());

            // BlobClient를 통해 특정 Blob을 선택
            BlobClient blobClient = containerClient.getBlobClient("question-images/" + encodedFileName);

            // 파일 업로드
            try (InputStream fileStream = file.getInputStream()) {
                blobClient.upload(fileStream, file.getSize(), true);  // `true`는 파일이 이미 존재하면 덮어쓰도록 설정
                return ResponseEntity.ok("File uploaded successfully: " + blobClient.getBlobUrl());
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading file to Azure: " + e.getMessage());
        }
    }
}
