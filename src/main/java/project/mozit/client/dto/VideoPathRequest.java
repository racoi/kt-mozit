package project.mozit.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VideoPathRequest {

    @JsonProperty("video_path") // JSON 필드 이름 명시
    private String videoPath;

    @JsonProperty("output_path")
    private String outputPath;

    // Getter and Setter
    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public String toString() {
        return "VideoPathRequest{" +
                "videoPath='" + videoPath + '\'' +
                ", outputPath='" + outputPath + '\'' +
                '}';
    }
}
