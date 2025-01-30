package project.mozit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.mozit.client.api.FastApiClient;
import project.mozit.client.dto.VideoPathRequest;
import project.mozit.client.dto.VideoResponse;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class FastApiService {

    @Autowired
    private FastApiClient fastApiClient;

    @Autowired
    private RestTemplate restTemplate; // RestTemplate 주입

    private ConcurrentHashMap<String, VideoResponse> responseCache = new ConcurrentHashMap<>();

    public VideoResponse sendVideoPath(String videoPath, String outputPath) {
        String fastApiUrl = "http://localhost:8000/process-video/";

        VideoPathRequest videoPathRequest = new VideoPathRequest();
        videoPathRequest.setVideoPath(videoPath);
        videoPathRequest.setOutputPath(outputPath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<VideoPathRequest> requestEntity = new HttpEntity<>(videoPathRequest, headers);

        ResponseEntity<List<VideoResponse.FrameInfo>> response = restTemplate.exchange(
                fastApiUrl,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<VideoResponse.FrameInfo>>() {}
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            List<VideoResponse.FrameInfo> frameInfos = response.getBody();
            if (frameInfos != null) {
                VideoResponse videoResponse = new VideoResponse();
                videoResponse.setFrames(frameInfos);
                responseCache.put(videoPath, videoResponse); // 캐시에 저장
                return videoResponse; // VideoResponse 반환
            }
        }
        return null; // 실패 시 null 반환
    }





    public List<VideoResponse.FrameInfo> getVideoResponse(String videoPath) {
        VideoResponse videoResponse = responseCache.get(videoPath);
        if (videoResponse != null) {
            return videoResponse.getFrames();
        }
        return null; // 비디오 응답이 없을 경우 null 반환
    }
}