package project.mozit.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import project.mozit.util.RedisUtil;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String senderEmail;

    private String createCode() {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 | i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private String setContext(String authCode) {
        Context context = new Context();
        context.setVariable("authCode", authCode); // 인증 코드 변수 설정
        return templateEngine.process("auth-email", context);
    }

    private MimeMessage createEmailForm(String email) throws MessagingException {
        String authCode = createCode();

        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("MOZIT 메일 인증 코드 입니다.");

        message.setText(setContext(authCode), "utf-8", "html");
        message.setFrom(senderEmail);

        redisUtil.setDataExpire(email, authCode, 60 * 5L);

        return message;
    }

    public void sendEmail(String toEmail) throws MessagingException {
        String requestKey = "request_limit:" + toEmail;
        String requestCount = redisUtil.getData(requestKey);

        if (requestCount != null && Integer.parseInt(requestCount) >= 100) {
            throw new IllegalArgumentException("1시간 내 최대 요청 횟수를 초과했습니다. 잠시 후 다시 시도해주세요.");
        }

        // 요청 횟수 증가 및 제한 시간 설정 (예: 1시간 동안 최대 5회)
        redisUtil.setDataExpire(requestKey, String.valueOf((requestCount == null ? 1 : Integer.parseInt(requestCount) + 1)), 3600);

        MimeMessage emailForm = createEmailForm(toEmail);
        javaMailSender.send(emailForm);
    }

    public Boolean verifyEmailCode(String email, String code) {
        String codeFoundByEmail = redisUtil.getData(email);
        if (codeFoundByEmail == null) {
            throw new IllegalArgumentException("인증 코드가 유효하지 않습니다.");
        }

        if (codeFoundByEmail.equals(code)) {
            redisUtil.deleteData(email);
            return codeFoundByEmail.equals(code);
        } else {
            throw new IllegalArgumentException("인증 코드가 유효하지 않습니다.");
        }
    }
}
