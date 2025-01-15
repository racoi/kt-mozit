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

    // 파일 업로드 처리
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

    // 파일 다운로드 처리
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
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
