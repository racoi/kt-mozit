package project.mozit.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

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
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private String noticeTitle;
        private String noticeDetail;
    }
}
