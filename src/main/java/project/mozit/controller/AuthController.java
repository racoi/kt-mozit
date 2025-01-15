package project.mozit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.mozit.util.JWTUtil;
import project.mozit.util.RedisUtil;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final JWTUtil jwtUtil;
    private final RedisUtil redisUtil;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Refresh-Token") String refreshToken) {
        try {
            if (jwtUtil.isExpired(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired");
            }

            String username = jwtUtil.getUsername(refreshToken);
            String storedToken = redisUtil.getData(username);

            if (storedToken == null || !storedToken.equals(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
            }

            String role = jwtUtil.getRole(refreshToken);
            String newAccessToken = jwtUtil.createAccessToken(username, role);

            return ResponseEntity.ok().header("Authorization", "Bearer " + newAccessToken).body("Access token refreshed");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token: " + e.getMessage());
        }
    }
}
