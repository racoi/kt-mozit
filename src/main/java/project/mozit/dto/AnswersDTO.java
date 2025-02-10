package project.mozit.dto;

import lombok.Data;
import project.mozit.domain.Questions;

import java.time.OffsetDateTime;

public class AnswersDTO {

    @Data
    public static class Post{
        private Long questionNum;
        private String answerDetail;
    }

    @Data
    public static class Response{
        private Long answerNum;
        private OffsetDateTime timestamp;
        private String answerDetail;
    }
}
