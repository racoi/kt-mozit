package project.mozit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class ThymeleafConfig {
    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/"); // 템플릿 파일 경로
        templateResolver.setSuffix(".html"); // 템플릿 파일 확장자
        templateResolver.setTemplateMode("HTML"); // 템플릿 모드 (HTML, TEXT 등)
        templateResolver.setCacheable(false); // 캐싱 여부
        return templateResolver;
    }

    @Bean
    @Primary
    public TemplateEngine customTemplateEngine(ClassLoaderTemplateResolver templateResolver) {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }
}
