package project.mozit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 경로에 대해 CORS 설정
                .allowedOriginPatterns("https://*.azurestaticapps.net") // React 앱의 URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 필요한 메서드 추가
                .allowedHeaders("*")  // 모든 헤더 허용
                .exposedHeaders("Authorization", "Temporary-Token")  // 클라이언트가 Temporary-Token을 볼 수 있게 설정
                .allowCredentials(true);
    }
}
