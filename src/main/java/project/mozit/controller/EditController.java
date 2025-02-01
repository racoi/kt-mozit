package project.mozit.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.mozit.client.dto.VideoPathRequest;
import project.mozit.client.dto.VideoResponse;
import project.mozit.dto.EditsDTO;
import project.mozit.dto.MosaicStatusRequest;
import project.mozit.service.EditService;
import project.mozit.service.FastApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/edit")
public class EditController {

    private final EditService editService;
    // 모자이크 상태를 클래스명별로 저장
    private Map<String, Boolean> mosaicStatus = new HashMap<>();

    private static final String UPLOAD_DIR = "C:/Users/User/mini7/kt-mozit/temp"; // 비디오 파일이 저장된 경로

    private static final Logger log = LoggerFactory.getLogger(EditController.class);
    @Autowired
    private FastApiService fastApiService;

    public EditController(EditService editService) {
        this.editService = editService;
    }

    // 파일 업로드
    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("videoFile") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일이 첨부되지 않았습니다.");
        }

        try {
            String savedFileName = editService.uploadFile(file);
            return ResponseEntity.ok(savedFileName); // 파일 이름만 반환
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 업로드 실패: " + e.getMessage());
        }
    }




    // 편집 시작-DB에 저장
    @PostMapping("/start-editing")
    public ResponseEntity<Object> startEditing(@RequestParam("videoFile") MultipartFile  file, @RequestHeader("Authorization") String token) {
        try {
            // 1. 파일 저장
            String savedFileName = editService.uploadFile(file);
            log.info("파일 저장 성공: {}", savedFileName);

            // 2. 썸네일 생성
            String thumbnailPath = editService.extractThumbnail(savedFileName);
            log.info("썸네일 생성 성공: {}", thumbnailPath);

            // 3. DB 저장 및 editNum 반환
            Long editNum = editService.saveStartEditing(thumbnailPath, token);
            log.info("DB 저장 성공, editNum: {}", editNum);

            // 응답 객체 생성
            Map<String, Object> response = new HashMap<>();
            response.put("editNum", editNum);
            response.put("savedFileName", savedFileName);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            log.error("파일 저장 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 저장 실패: " + e.getMessage());
        } catch (Exception e) {
            log.error("처리 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("오류 발생: " + e.getMessage());
        }
    }

    @PostMapping("/send-video-path")
    public ResponseEntity<?> sendVideoPath(@RequestBody VideoPathRequest videoPathRequest) {
        System.out.println("Received videoPath: " + videoPathRequest.getVideoPath());
        System.out.println("Received outputPath: " + videoPathRequest.getOutputPath());

        try {
            // FastAPI에 비디오 경로를 전송하고 응답 받기
            VideoResponse videoResponse = fastApiService.sendVideoPath(
                    videoPathRequest.getVideoPath(),
                    videoPathRequest.getOutputPath()
            );

            if (videoResponse != null) {
                return ResponseEntity.ok(videoResponse); // VideoResponse 반환
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to process video path.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send video path: " + e.getMessage());
        }
    }





    //편집 화면에서 동영상 불러오기
    @GetMapping("/videos/{fileName}")
    public ResponseEntity<FileSystemResource> getVideo(@PathVariable("fileName") String fileName) {
        System.out.println("Requested video file: " + fileName);
        File file = new File(UPLOAD_DIR, fileName);  // /temp 디렉토리 경로

        if (file.exists()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, "video/mp4"); // 또는 해당 비디오 형식
            return new ResponseEntity<>(new FileSystemResource(file), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


       @GetMapping("/videos/{fileName}/info")
    public ResponseEntity<?> getVideoInfo(@PathVariable("fileName") String fileName) {
        List<VideoResponse.FrameInfo> frameInfos = fastApiService.getVideoResponse(fileName);

        if (frameInfos == null || frameInfos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No video response found.");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("videoUrl", "http://localhost:8080/videos/" + fileName);
        response.put("detections", frameInfos); // 모든 감지 데이터를 추가

        return ResponseEntity.ok(response);
    }






/*

    // 모자이크 상태 업데이트
    @PostMapping("/mosaic-status")
    public ResponseEntity<String> updateMosaicStatus(@RequestBody MosaicStatusRequest request) {
        // 클래스별 모자이크 활성화 상태 저장
        editService.updateMosaicStatus(request);  // Service에서 처리
        return ResponseEntity.ok("모자이크 상태 업데이트 성공");
    }

    // 처리된 프레임 반환
    @GetMapping("/processed-frame")
    public ResponseEntity<byte[]> getProcessedFrame(@RequestParam("editNum") int editNum) {
        // editNum에 해당하는 프레임을 처리하여 반환
        byte[] processedFrame = editService.processFrame(editNum); // Service에서 처리
        return ResponseEntity.ok(processedFrame);
    }



*/





    // 파일 다운로드 및 DB에 저장
//    @PostMapping("/download")
//    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName,
//                                                 @RequestParam("editNum") Long editNum) {
//        if (fileName == null || fileName.isBlank()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//
//        try {
//            // DB에 다운로드 정보 저장
//            editService.saveDownloadInfo(fileName, editNum);
//            return editService.downloadFile(fileName);  //동영상 다운로드
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
    @PostMapping("/download")// 임시 다운로드 (동영상 다운로드 서비스 주석처리함.)
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName,
                                                 @RequestParam("editNum") Long editNum) {
        if (fileName == null || fileName.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
            // DB에 다운로드 정보 저장
            editService.saveDownloadInfo(fileName, editNum);

            // 파일 다운로드 로직 주석 처리
            // return editService.downloadFile(fileName);  // 동영상 다운로드

            // 주석 처리 후 대체 응답 (예: 성공 메시지)
            return ResponseEntity.ok().body(null); // 또는 적절한 응답 객체를 반환하세요
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }




    // 다시 편집 요청
    @PostMapping("/restart-editing")
    public ResponseEntity<Long> startReEditing(@RequestBody Map<String, String> requestBody, @RequestHeader("Authorization") String token) {
        String videoFileName = requestBody.get("videoFileName");

        try {
            // 1. 썸네일 생성
            String thumbnailPath = editService.extractThumbnail(videoFileName);
            log.info("썸네일 생성 성공: {}", thumbnailPath);
            // 2. DB에 저장
            Long editNum = editService.saveStartEditing(thumbnailPath, token);
            return ResponseEntity.ok(editNum);// EDIT_NUM 반환
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    // 사용자 ID로 edit 목록을 가져오는 GET 요청
    @GetMapping
    public ResponseEntity<List<EditsDTO>> getUserEdits(@RequestHeader("Authorization") String token) {
        try {
            List<EditsDTO> edits = editService.getEditsByUserId(token); // 서비스 메서드 호출

            if (edits.isEmpty()) {
                return ResponseEntity.noContent().build(); // 204 No Content
            }

            return ResponseEntity.ok(edits); // 200 OK와 함께 edit 목록 반환
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 Internal Server Error
        }
    }

}

