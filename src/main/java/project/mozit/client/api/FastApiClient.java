package project.mozit.client.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import project.mozit.client.dto.VideoPathRequest;
import project.mozit.client.dto.VideoResponse;

@FeignClient(name = "fastApiClient", url = "${mozit.api.host}")
public interface FastApiClient {

    @PostMapping("/process-video/")
    ResponseEntity<VideoResponse> uploadVideo(@RequestBody VideoPathRequest videoPathRequest);
}
