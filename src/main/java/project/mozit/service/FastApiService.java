package project.mozit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.mozit.client.api.FastApiClient;
import project.mozit.client.dto.VideoPathRequest;
import project.mozit.client.dto.VideoResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class FastApiService {

    @Autowired
    private FastApiClient fastApiClient;

    // FastAPI로 동영상 경로를 보내고 응답을 받는 메서드
    public void sendVideoPath(String videoPath, String outputPath) {
        String fastApiUrl = "http://localhost:8000/process-video/";

        VideoPathRequest videoPathRequest = new VideoPathRequest();
        videoPathRequest.setVideoPath(videoPath);
        videoPathRequest.setOutputPath(outputPath);

        // JSON 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 본문 설정
        HttpEntity<VideoPathRequest> requestEntity = new HttpEntity<>(videoPathRequest, headers);

        // POST 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("FastAPI 요청 하자 제발: " + requestEntity.getBody());
        System.out.println("Headers: " + requestEntity.getHeaders());

        ResponseEntity<String> response = restTemplate.postForEntity(fastApiUrl, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("FastAPI 요청 성공: " + response.getBody());
        } else {
            System.err.println("FastAPI 요청 실패: " + response.getBody());
        }
    }

}
