package project.mozit.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.mozit.service.EditService;

import java.io.IOException;

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
    public ResponseEntity<Long> startEditing(@RequestParam("videoFileName") String videoFileName) {

        try {
//            editService.sendVideoPathToFastAPI(videoFileName);
            String thumbnail = "/path/to/thumbnail.jpg";
            Long editNum = editService.saveStartEditing(thumbnail);
            return ResponseEntity.ok(editNum);// EDIT_NUM 반환
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
}
