package project.mozit.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import project.mozit.domain.Users;
import project.mozit.dto.CustomUserDetails;
import project.mozit.repository.UsersRepository;
import project.mozit.util.JWTUtil;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UsersRepository usersRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // 인증이 필요 없는 경로 제외
        if (isPublicEndpoint(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");

        // Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Authorization header missing or invalid");
            return;
        }

        String token = authorization.substring(7); // Bearer 제거

        try {
            // 토큰 만료 여부 확인
            if (jwtUtil.isExpired(token)) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
                return;
            }

            // 유효한 토큰 처리
            String username = jwtUtil.getUsername(token);
            String role = jwtUtil.getRole(token);

            Users user = usersRepository.findByUserId(username);
            CustomUserDetails customUserDetails = new CustomUserDetails(user);

            Authentication authToken = new UsernamePasswordAuthenticationToken(
                    customUserDetails, null, customUserDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
            System.out.println("Authentication set in SecurityContext: " + SecurityContextHolder.getContext().getAuthentication());

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token: " + e.getMessage());
        }
    }

    private boolean isPublicEndpoint(String requestURI) {
//        return "/users/login".equals(requestURI) || "/auth/refresh".equals(requestURI)
//                || "/users/check-id".equals(requestURI) || "/users/signup".equals(requestURI)
//                || "/users/send-email".equals(requestURI) || "/users/verify-email".equals(requestURI)
//                || "/users/find-id".equals(requestURI) || "/users/find-pw".equals(requestURI)
//                || "/h2-console".equals(requestURI);
        return true;
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        if (!response.isCommitted()) { // 응답이 이미 커밋되지 않았는지 확인
            response.setStatus(status);
            response.setContentType("application/json;charset=UTF-8");

            // OutputStream 사용
            String jsonResponse = "{\"error\": \"" + message + "\"}";
            response.getOutputStream().write(jsonResponse.getBytes("UTF-8"));
            response.getOutputStream().flush();
        }
    }
}
