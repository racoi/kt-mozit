package project.mozit.dto;

import lombok.Data;

import java.time.LocalDateTime;

public class NoticesDTO {

    @Data
    public static class Post{
        private String noticeTitle;
        private String noticeDetail;
    }

    @Data
    public static class Patch{
        private String noticeTitle;
        private String noticeDetail;
    }

    @Data
    public static class Response{
        private Long noticeNum;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String noticeTitle;
        private String noticeDetail;
    }
}
