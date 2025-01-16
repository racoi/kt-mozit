package project.mozit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.mozit.dto.UsersDTO;
import project.mozit.service.MyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my")
public class MyController {
    public final MyService myService;

    @PostMapping("/verify-password")
    public ResponseEntity<String> verifyPassword(@RequestHeader("Authorization") String token, @RequestBody String password) {
        try {
            boolean isVerified = myService.verifyPassword(token, password);
            if (isVerified) {
                return ResponseEntity.ok("비밀번호가 확인되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호 확인 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
        try {
            UsersDTO.Response userInfo = myService.getUserInfo(token);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("개인정보 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PatchMapping
    public ResponseEntity<String> updateUserInfo(
            @RequestHeader("Authorization") String token,
            @RequestBody UsersDTO.Patch patchDto) {
        try {
            myService.updateUserInfo(token, patchDto);
            return ResponseEntity.ok("사용자 정보가 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("사용자 정보 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
