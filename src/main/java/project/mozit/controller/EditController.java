package project.mozit.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.mozit.domain.Edits;
import project.mozit.dto.EditsDTO;
import project.mozit.service.EditService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/edit")
public class EditController {

    private final EditService editService;

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
    public ResponseEntity<Long> startEditing(@RequestParam("videoFileName") String videoFileName, @RequestHeader("Authorization") String token) {
        try {
            String thumbnail = editService.extractThumbnail(videoFileName);
            Long editNum = editService.saveStartEditing(thumbnail, token);
            return ResponseEntity.ok(editNum); // EDIT_NUM 반환
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }





    // 파일 다운로드 및 DB에 저장
    @PostMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName,
                                                 @RequestParam("editNum") Long editNum) {
        if (fileName == null || fileName.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
            // DB에 다운로드 정보 저장
            editService.saveDownloadInfo(fileName, editNum);
            return editService.downloadFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 다시 편집 요청
    @PostMapping("/restart-editing")
    public ResponseEntity<Long> startReEditing(@RequestParam("videoFileName") String videoFileName, @RequestHeader("Authorization") String token) {

        try {
//            editService.sendVideoPathToFastAPI(videoFileName);
            String thumbnail = "/path/to/thumbnail.jpg";
            Long editNum = editService.saveStartEditing(thumbnail, token);
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
