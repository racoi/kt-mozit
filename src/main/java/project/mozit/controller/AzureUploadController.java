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
            // SAS 토큰 URL 인코딩
            String encodedSasToken = encodeSasToken(sasToken);

            // Azure Blob Storage 클라이언트 생성
            String blobServiceUrl = "https://" + storageAccountName + ".blob.core.windows.net?" + encodedSasToken;
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .endpoint(blobServiceUrl)
                    .buildClient();

            // 컨테이너 클라이언트 가져오기
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            // 파일 이름 생성
            String blobName = "question-images/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            BlockBlobClient blobClient = containerClient.getBlobClient(blobName).getBlockBlobClient();

            // 파일을 Blob에 업로드
            try (InputStream fileInputStream = file.getInputStream()) {
                blobClient.upload(fileInputStream, file.getSize(), true);
            }

            // 업로드된 파일의 URL 반환
            return blobClient.getBlobUrl();
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file to Azure", e);
        }
    }

    // SAS 토큰을 URL 인코딩하는 메서드
    private String encodeSasToken(String sasToken) {
        try {
            return URLEncoder.encode(sasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error encoding SAS token", e);
        }
    }
}
