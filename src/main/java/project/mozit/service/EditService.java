package project.mozit.service;

//import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import project.mozit.client.api.FastApiClient;
import project.mozit.client.dto.VideoPathRequest;
import project.mozit.client.dto.VideoResponse;
import project.mozit.domain.Downloads;
import project.mozit.domain.Edits;
import project.mozit.domain.Users;
import project.mozit.dto.DownloadsDTO;
import project.mozit.dto.EditsDTO;
import project.mozit.mapper.DownloadsMapper;
import project.mozit.repository.DownloadsRepository;
import project.mozit.repository.EditsRepository;
import project.mozit.repository.UsersRepository;
import project.mozit.util.JWTUtil;
import project.mozit.util.ThumbnailUtil;
import project.mozit.client.dto.VideoPathRequest;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class EditService {
    private static final String UPLOAD_DIR = "temp"; // 파일 업로드 경로
    @Autowired
    private EditsRepository editsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private DownloadsRepository downloadsRepository;

    @Autowired
    private DownloadsMapper downloadsMapper;

    @Autowired
    private FastApiClient fastApiClient;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private FastApiService fastApiService;

    public String getUsername(String token){
        return jwtUtil.getUsername(token.replace("Bearer ", ""));
    }
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



//// 동영상 경로를 FastAPI에 보내고 응답을 받는 로직
//    public VideoResponse sendVideoData(String videoPath) {
//        return fastApiService.sendVideoData(videoPath);
//    }

    // 썸네일 추출 메서드
    public String extractThumbnail(String videoFileName) throws IOException {
        String videoPath = Paths.get(UPLOAD_DIR, videoFileName).toString();

        // 체크: 파일이 존재하는지 확인
        File videoFile = new File(videoPath);
        if (!videoFile.exists()) {
            throw new IOException("비디오 파일이 존재하지 않습니다: " + videoPath);
        }

        String thumbnailPath = Paths.get(UPLOAD_DIR, "thumbnail-" + videoFileName + ".jpg").toString();
        ThumbnailUtil.extractThumbnail(videoPath, thumbnailPath);
        return thumbnailPath; // 썸네일 경로 반환
    }


    // 편집 시작 내용 DB에 저장
    @Transactional
    public Long  saveStartEditing(String thumbnail, String token) {
        String userId = getUsername(token);
        Users userNum = usersRepository.findByUserId(userId);
        if (userNum == null) {
            throw new EntityNotFoundException("해당 유저를 찾을 수 없습니다. ID: " + userId);
        }
        Edits edits = new Edits();

        edits.setThumbnail(thumbnail); //1. 썸네일 경로
        edits.setTimestamp(LocalDateTime.now()); // 2. 현재 시간 설정
        edits.setUserNum(userNum); // Users 객체를 설정

        // DB에 저장
        Edits savedEdits = editsRepository.save(edits); // Edits 도메인 객체를 직접 저장
        System.out.println(" 저장 성공: " + thumbnail);
        return savedEdits.getEditNum(); // 저장된 EDIT_NUM 반환
    }


    //다운로드시 모자이크 처리할 내용 DB에 저장.
    @Transactional
    public void saveDownloadInfo(String fileName, Long editNum) {
        Downloads downloads = new Downloads();
        downloads.setFaceMosaic(false); // 하드코딩된
        downloads.setHazardousList("칼, 총, 피"); // 하드코딩된
        downloads.setPersonalList("민증, 여권"); // 하드코딩된

        // EDIT_NUM 설정
        Edits edit = new Edits();
        edit.setEditNum(editNum);
        downloads.setEditNum(edit);

        // 다운로드 정보 저장
        downloadsRepository.save(downloads);
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



    //내 작업 목록 가져오기
    public List<EditsDTO> getEditsByUserId(String token) {
        String userId = getUsername(token);
        Users userNum = usersRepository.findByUserId(userId);

        if (userNum == null) {
            throw new EntityNotFoundException("해당 유저를 찾을 수 없습니다. ID: " + userId);
        }

        List<Edits> edits = editsRepository.findByUserNum(userNum);

        // EditResponseDTO 리스트로 변환
        List<EditsDTO> response = new ArrayList<>();
        for (Edits edit : edits) {
            // 다운로드 존재 여부 확인
            boolean hasDownload = downloadsRepository.existsByEditNum(edit);

            response.add(new EditsDTO(
                    edit.getEditNum(),
                    edit.getThumbnail(),
                    edit.getTimestamp(),
                    userNum.getUserNum(), // userNum 추가
                    hasDownload // 다운로드 여부만 포함
            ));
        }

        return response;
    }


}