package project.mozit.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.mozit.client.dto.VideoPathRequest;
import project.mozit.client.dto.VideoResponse;
import project.mozit.dto.DownloadsDTO;
import project.mozit.dto.EditsDTO;
import project.mozit.service.EditService;
import project.mozit.service.FastApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/edit")
public class EditController {

    private final EditService editService;
    // 모자이크 상태를 클래스명별로 저장
    private Map<String, Boolean> mosaicStatus = new HashMap<>();

//    private static final String UPLOAD_DIR = "D:\\home\\uploads"; // 비디오 파일이 저장된 경로

    private static final Logger log = LoggerFactory.getLogger(EditController.class);


    @Autowired
    private RestTemplate restTemplate;

    @Value("${mozit.api.host}")
    private String apiHost;

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
    public ResponseEntity<Object> startEditing(@RequestBody Map<String, String> requestBody, @RequestHeader("Authorization") String token) {
        String videoUrl = requestBody.get("videoUrl");
        try {
            // 1. 비디오 URL을 통해 썸네일 생성 요청
            String thumbnailUrl = editService.captureThumbnail(videoUrl);
            log.info("썸네일 생성 성공: {}", thumbnailUrl);

            // 2. DB 저장 및 editNum 반환
            Long editNum = editService.saveStartEditing(thumbnailUrl, token);
            log.info("DB 저장 성공, editNum: {}", editNum);

            // 응답 객체 생성
            Map<String, Object> response = new HashMap<>();
            response.put("editNum", editNum);

            return ResponseEntity.ok(response);
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
        File file = new File(fileName);  // /temp 디렉토리 경로

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
//        response.put("videoUrl", apiHost + "/videos/" + fileName);
        response.put("detections", frameInfos); // 모든 감지 데이터를 추가

        return ResponseEntity.ok(response);
    }



// 편집 완료시 제목 넣기
    @PutMapping("/{editNum}")
    public ResponseEntity<Void> updateEditTitle(@PathVariable("editNum") Long editNum, @RequestBody EditsDTO editsDTO) {
        if (editsDTO.getEditTitle() == null || editsDTO.getEditTitle().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 제목이 비어있으면 400 Bad Request
        }

        editService.updateEditTitle(editNum, editsDTO.getEditTitle());
        return ResponseEntity.ok().build(); // 200 OK
    }






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

    @PostMapping("/download")
    public ResponseEntity<Void> downloadFile(@RequestBody DownloadsDTO downloadsDTO) {
        Long editNum = downloadsDTO.getEditNum().getEditNum(); // 편집 번호 가져오기
        Boolean faceMosaic = downloadsDTO.getFaceMosaic(); // 얼굴 모자이크 여부
        String hazardousList = downloadsDTO.getHazardousList(); // 유해 요소 목록
        String personalList = downloadsDTO.getPersonalList(); // 개인정보 목록

        if (editNum == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            // DB에 다운로드 정보 저장
            editService.saveDownloadInfo(editNum, faceMosaic, hazardousList, personalList);

            // 파일 다운로드 로직 구현 필요=>프런트에서
            //return editService.downloadFile(fileName); // 동영상 다운로드

            // 성공 응답
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }





    // 다시 편집 요청
    @PostMapping("/restart-editing")
    public ResponseEntity<Long> startReEditing(@RequestBody Map<String, String> requestBody, @RequestHeader("Authorization") String token) {
        String videoFileName = requestBody.get("videoFileName");

        try {
            // 1. 썸네일 생성
            String thumbnailPath = editService.captureThumbnail(videoFileName);
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

