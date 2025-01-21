package project.mozit.client.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import project.mozit.client.dto.VideoPathRequest;
import project.mozit.client.dto.VideoResponse;

import java.util.Map;

@FeignClient(name = "fastApiClient", url = "${mozit.api.host}")
public interface FastApiClient {

    @PostMapping("/upload_video")
    ResponseEntity<Map<String, Object>> uploadVideo(@RequestBody VideoPathRequest videoPathRequest);
}
