package project.mozit.service;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.mozit.domain.Edits;
import project.mozit.domain.Users;
import project.mozit.repository.EditsRepository;
import project.mozit.repository.UsersRepository;
//import project.mozit.client.dto.VideoPathRequest;
//import project.mozit.client.api.FastApiClient;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EditService {
    private static final String UPLOAD_DIR = "temp"; // 파일 업로드 경로
    @Autowired
    private EditsRepository editsRepository;

    @Autowired
    private UsersRepository usersRepository;

//    @Autowired
//    private FastApiClient fastApiClient;

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

/*
    // FastAPI에 동영상 경로 전송
    public void sendVideoPathToFastAPI(String videoFileName) {
        Path videoPath = Paths.get(UPLOAD_DIR, videoFileName);
        VideoPathRequest videoPathRequest = new VideoPathRequest(videoPath.toString());

        try {
            fastApiClient.uploadVideo(videoPathRequest);
        } catch (Exception e) {
            System.err.println("FastAPI에 동영상 경로 전송 실패: " + e.getMessage());
        }
    }
*/

    // 편집 시작 및 DB에 저장
    @Transactional
    public void saveStartEditing(String thumbnail) {
        Edits edits = new Edits();

        edits.setThumbnail(thumbnail); // FastAPI로부터 받은 썸네일 설정
        //String thumbnail = "/path/to/thumbnail.jpg"; // 나중에 바꿔야 함.
        edits.setThumbnail(thumbnail);

        edits.setTimestamp(LocalDateTime.now()); // 현재 시간 설정

        // 사용자 ID가 1인 사용자 조회(일단 임시로 ) 나중에 바꿔야 함.
        Users user = usersRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다.")); // 사용자 존재 여부 확인
        edits.setUserNum(user); // Users 객체를 설정

        // DB에 저장
        editsRepository.save(edits); // Edits 도메인 객체를 직접 저장
        System.out.println(" 저장 성공: " + thumbnail);
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
