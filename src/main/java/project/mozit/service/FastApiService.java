package project.mozit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.mozit.client.api.FastApiClient;
import project.mozit.client.dto.VideoPathRequest;

import java.util.Map;

@Service
public class FastApiService {

    @Autowired
    private FastApiClient fastApiClient;

    // FastAPI로 동영상 경로를 보내고 응답을 받는 메서드
    public void sendVideoPath(String videoPath) {
        VideoPathRequest request = new VideoPathRequest();
        request.setVideo_path(videoPath); // video_path 필드 설정

        ResponseEntity<Map<String, Object>> response = fastApiClient.uploadVideo(request);

        // 응답 처리
        System.out.println("FastAPI Response: " + response.getBody());
    }
}
