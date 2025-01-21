package project.mozit.client.dto;

public class VideoResponse {
    private String message;
    private String videoPath;

    // Getter 및 Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
}
