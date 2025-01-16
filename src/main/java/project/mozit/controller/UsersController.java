package project.mozit.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.mozit.dto.EmailDTO;
import project.mozit.dto.UsersDTO;
import project.mozit.service.EmailService;
import project.mozit.service.SignUpService;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UsersController {

    private final SignUpService signUpService;
    private final EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<String> joinProcess(@RequestBody UsersDTO.Post usersDto){
        signUpService.joinProcess(usersDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    @GetMapping("/logout-success")
    public ResponseEntity<String> logoutSuccess() {
        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/check-id")
    public ResponseEntity<String> checkUserId(@RequestParam("userId") String userId) {
        boolean isUserExists = signUpService.checkUserId(userId);

        if (isUserExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 아이디입니다."); // 409 Conflict
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body("사용 가능한 아이디입니다."); // 201 Created
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
            return ResponseEntity.ok("인증이 완료되었습니다."); // 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 실패하셨습니다."); // 400 Bad Request
        }
    }
}
