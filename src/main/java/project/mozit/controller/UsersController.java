package project.mozit.controller;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.mozit.dto.EmailDTO;
import project.mozit.dto.UsersDTO;
import project.mozit.service.EmailService;
import project.mozit.service.UsersService;
import project.mozit.util.JWTUtil;

import java.util.Map;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;
    private final EmailService emailService;
    private final JWTUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<String> joinProcess(@RequestBody UsersDTO.Post usersDto){
        usersService.joinProcess(usersDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    @GetMapping("/logout-success")
    public ResponseEntity<String> logoutSuccess() {
        return ResponseEntity.ok("로그아웃 성공");
    }

    @GetMapping("/check-id")
    public ResponseEntity<String> checkUserId(@RequestParam("userId") String userId) {
        boolean isUserExists = usersService.checkUserId(userId);

        if (isUserExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 아이디입니다."); // 409 Conflict
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body("사용 가능한 아이디입니다."); // 201 Created
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<String> checkUserEmail(@RequestParam("userEmail") String userEmail) {
        boolean isUserExists = usersService.checkUserEmail(userEmail);

        if (isUserExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 이메일입니다."); // 409 Conflict
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body("사용 가능한 이메일입니다."); // 201 Created
        }
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendmail(@RequestBody EmailDTO emailDto) throws MessagingException {
        try {
            System.out.println("EmailController.mailSend() " + emailDto.getMail());
            emailService.sendEmail(emailDto.getMail());
            return ResponseEntity.ok("이메일 전송이 완료되었습니다.");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이메일 전송에 실패했습니다. 오류 메시지: " + e.getMessage());
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailDTO emailDto) {
        boolean isVerify = emailService.verifyEmailCode(emailDto.getMail(), emailDto.getVerifyCode());

        if (isVerify) {
            String temporaryToken = jwtUtil.createTemporalToken(emailDto.getMail(), "ROLE_USER");
            return ResponseEntity.ok().header("Temporary-Token", "Bearer " + temporaryToken)
                    .body("인증이 완료되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 실패하셨습니다."); // 400 Bad Request
        }
    }

    @PostMapping("/find-id")
    public ResponseEntity<String> findId(@RequestParam("email") String email) {
        return usersService.findUserId(email)
                .map(userId -> ResponseEntity.ok(userId))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 이메일로 사용자를 찾을 수 없습니다.")); // 실패 시 메시지 반환
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestHeader("Authorization") String temporaryToken,
            @RequestBody Map<String, String> request) {

        try {
            String pwd = request.get("new-pwd");
            String token = temporaryToken.replace("Bearer ", "");
            if (jwtUtil.isExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다.");
            }

            String email = jwtUtil.getUsername(token);
            usersService.updatePassword(email, pwd);

            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호 변경 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

}
