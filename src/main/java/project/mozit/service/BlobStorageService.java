package project.mozit.service;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class BlobStorageService {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    private BlobServiceClient blobServiceClient;

    @PostConstruct
    public void init() {
        if(connectionString == null || connectionString.isEmpty()) {
            throw new RuntimeException("ConnectionString not set");
        }

        this.blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }

    // BlobContainerClient 가져오기
    public void uploadFile(String containerName, String blobName, byte[] content) {
        var containerClient = blobServiceClient.getBlobContainerClient(containerName);
        var blobClient = containerClient.getBlobClient(blobName);

        blobClient.upload(BinaryData.fromBytes(content), true);
    }

    public byte[] downloadFile(String containerName, String blobName) {
        var containerClient = blobServiceClient.getBlobContainerClient(containerName);
        var blobClient = containerClient.getBlobClient(blobName);

        return blobClient.downloadContent().toBytes();
    }

    // 이미지를 썸네일로 업로드
    public void uploadThumbnail(String containerName, String blobName, File thumbnailFile) throws IOException {
        var containerClient = blobServiceClient.getBlobContainerClient(containerName);
        var blobClient = containerClient.getBlobClient(blobName);

        blobClient.uploadFromFile(thumbnailFile.getAbsolutePath(), true);
    }

    public String getBlobUrl(String containerName, String blobPath) {
        BlobClient blobClient = blobServiceClient.getBlobContainerClient(containerName).getBlobClient(blobPath);
        return blobClient.getBlobUrl();
    }
}
