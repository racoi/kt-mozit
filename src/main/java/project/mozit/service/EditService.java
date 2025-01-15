package project.mozit.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class EditService {
    private static final String UPLOAD_DIR = "temp"; // 파일 업로드 경로

    // 파일 업로드 처리
    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("파일이 첨부되지 않았습니다.");
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new IOException("유효하지 않은 파일 이름입니다.");
        }

        // 고유 파일명 생성 (UUID 사용)
        String safeFileName = UUID.randomUUID().toString() + "-" + originalFileName;

        // 디렉토리 생성
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("디렉토리를 생성할 수 없습니다.");
        }

        // 파일 저장
        Path targetLocation = Paths.get(UPLOAD_DIR, safeFileName);
        file.transferTo(targetLocation);

        return safeFileName; // 저장된 파일 이름 반환
    }

    // 파일 다운로드 처리
    public ResponseEntity<Resource> downloadFile(String fileName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new IOException("파일을 찾을 수 없거나 읽을 수 없습니다. 경로: " + filePath.toString());
        }

        // 한글 파일명을 UTF-8로 인코딩 처리
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replace("+", "%20"); // 공백 처리

        // 파일 다운로드 응답 반환
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                .body(resource);
    }
}
