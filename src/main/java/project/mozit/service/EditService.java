package project.mozit.service;

import jakarta.persistence.EntityNotFoundException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import project.mozit.client.api.FastApiClient;
import project.mozit.domain.Downloads;
import project.mozit.domain.Edits;
import project.mozit.domain.Users;
import project.mozit.dto.DetectionDTO;
import project.mozit.dto.EditsDTO;
import project.mozit.dto.MosaicStatusRequest;
import project.mozit.mapper.DownloadsMapper;
import project.mozit.repository.DownloadsRepository;
import project.mozit.repository.EditsRepository;
import project.mozit.repository.UsersRepository;
import project.mozit.util.JWTUtil;
import project.mozit.util.ThumbnailUtil;


import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class EditService {
    private static final String UPLOAD_DIR = "temp"; // 파일 업로드 경로
    private Map<String, Boolean> mosaicStatus = new HashMap<>();

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
        // 파일 이름 확인
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new IOException("유효하지 않은 파일 이름입니다.");
        }

        // 디렉토리 생성
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("업로드 디렉토리를 생성할 수 없습니다.");
        }

        // 중복 파일 처리 로직
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String safeFileName = originalFileName;

        int count = 1;
        while (new File(UPLOAD_DIR, safeFileName).exists()) {
            safeFileName = baseName + "_" + count + extension; // 중복 시 숫자 추가
            count++;
        }

        // 파일 저장
        Path targetLocation = Paths.get(UPLOAD_DIR, safeFileName);
        file.transferTo(targetLocation);

        return safeFileName; // 저장된 파일 이름 반환
    }




    // 썸네일 추출 메서드
    public String extractThumbnail(String videoFileName) throws IOException {
        String videoPath = Paths.get(UPLOAD_DIR, videoFileName).toString();

        // 체크: 파일이 존재하는지 확인
        File videoFile = new File(videoPath);
        if (!videoFile.exists()) {
            throw new IOException("비디오 파일이 존재하지 않습니다: " + videoPath);
        }

        // 파일 이름에서 확장자 제거
        String baseName = videoFileName.substring(0, videoFileName.lastIndexOf('.'));
        String baseThumbnailName = "thumbnail-" + baseName + ".jpg";
        String thumbnailPath = Paths.get(UPLOAD_DIR, baseThumbnailName).toString();

        // 중복 체크 및 숫자 추가
        int count = 1;
        while (new File(thumbnailPath).exists()) {
            // 썸네일 이름에 숫자를 추가
            String newThumbnailName = "thumbnail-" + baseName + "-" + count + ".jpg";
            thumbnailPath = Paths.get(UPLOAD_DIR, newThumbnailName).toString();
            count++; // 숫자 증가
        }

        // 썸네일 추출
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

        edits.setEditTitle(edits.getEditTitle()); // *
        edits.setThumbnail(thumbnail); //1. 썸네일 경로
        edits.setTimestamp(LocalDateTime.now()); // 2. 현재 시간 설정
        edits.setUserNum(userNum); // Users 객체를 설정

        // DB에 저장
        Edits savedEdits = editsRepository.save(edits); // Edits 도메인 객체를 직접 저장
        System.out.println(" 저장 성공: " + thumbnail);
        return savedEdits.getEditNum(); // 저장된 EDIT_NUM 반환
    }

/*
    // 모자이크 상태 업데이트
    public void updateMosaicStatus(MosaicStatusRequest request) {
        // 클래스별 모자이크 활성화 상태 저장
        mosaicStatus.put(request.getClassName(), request.isActive());
    }

    // 처리된 프레임 반환
    public byte[] processFrame(int editNum) {
        // editNum에 해당하는 프레임을 로드 (예시로 가정)
        Mat frame = loadFrame(editNum); // 프레임 로딩 (loadFrame은 예시 메서드)

        // 저장된 모자이크 상태를 바탕으로 모자이크 처리
        for (Map.Entry<String, Boolean> entry : mosaicStatus.entrySet()) {
            String className = entry.getKey();
            boolean isActive = entry.getValue();

            // 활성화된 클래스만 모자이크 처리
            if (isActive) {
                // 해당 클래스에 해당하는 객체들 찾아서 모자이크 처리
                List<DetectionDTO> detections = getDetectionsForClass(className); // 클래스별 객체 목록
                applyMosaic(frame, detections); // 모자이크 처리
            }
        }

        // 처리된 프레임을 byte[]로 변환하여 반환
        return convertMatToByteArray(frame);
    }

    // 프레임을 로드하는 메서드 (예시 구현)
    private Mat loadFrame(int editNum) {
        // 실제 구현은 editNum에 맞는 동영상 프레임을 가져와야 합니다
        return new Mat(); // 임시로 빈 Mat 객체 반환
    }

    // 클래스별 객체들을 반환하는 메서드 (예시 구현)
    private List<DetectionDTO> getDetectionsForClass(String className) {
        // 실제로는 className에 해당하는 객체를 찾아야 함
        // 여기에 더미 데이터를 넣어도 좋습니다.
        return new ArrayList<>(); // 예시로 빈 리스트 반환
    }

    // 모자이크 적용 함수
    private void applyMosaic(Mat frame, List<DetectionDTO> detections) {
        for (DetectionDTO detection : detections) {
            if (detection.isActive()) { // 활성화된 객체에 대해서만 모자이크 처리
                Rect roi = new Rect((int) detection.getX(), (int) detection.getY(), (int) detection.getWidth(), (int) detection.getHeight());
                Mat submat = frame.submat(roi);
                Imgproc.resize(submat, submat, new Size(10, 10)); // 축소
                Imgproc.resize(submat, submat, roi.size()); // 다시 원래 크기로
                submat.copyTo(frame.submat(roi));
            }
        }
    }

    // Mat을 byte[]로 변환하는 함수
    private byte[] convertMatToByteArray(Mat frame) {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", frame, matOfByte);
        return matOfByte.toArray();
    }


*/


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
                    edit.getEditTitle(),
                    edit.getThumbnail(),
                    edit.getTimestamp(),
                    userNum.getUserNum(), // userNum 추가
                    hasDownload // 다운로드 여부만 포함
            ));
        }

        return response;
    }


}