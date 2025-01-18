package project.mozit.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.mozit.repository.UsersRepository;
import project.mozit.util.JWTUtil;
import project.mozit.util.RedisUtil;

import java.util.Arrays;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JWTUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UsersRepository usersRepository;

    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
        try {
            // HttpOnly 쿠키에서 Refresh Token 가져오기
            String refreshToken = Arrays.stream(request.getCookies())
                    .filter(cookie -> "refreshToken".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);

            if (refreshToken == null) {
                return ResponseEntity.status(401).body("Refresh token is missing");
            }

            if (jwtUtil.isExpired(refreshToken)) {
                return ResponseEntity.status(401).body("Refresh token expired");
            }

            String username = jwtUtil.getUsername(refreshToken);
            String storedToken = redisUtil.getData(username);

            if (storedToken == null || !storedToken.equals(refreshToken)) {
                return ResponseEntity.status(401).body("Invalid or expired refresh token");
            }

            // 사용자 정보 검색
            var user = usersRepository.findByUserId(username);

            String role = jwtUtil.getRole(refreshToken);
            String newAccessToken = jwtUtil.createAccessToken(username, role);

            // 전체 이름 포함하여 반환
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken)
                    .body(Map.of("username", user.getUserName())); // 사용자 전체 이름 반환
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token: " + e.getMessage());
        }
    }
}
