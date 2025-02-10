package project.mozit.dto;

import lombok.Data;
import project.mozit.domain.Questions;
import project.mozit.domain.Users;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class QuestionsDTO {

    @Data
    public static class Post{
        private String questionTitle;
        private String questionDetail;
        private Questions.QuestionType questionType;
        private String questionImage;
    }

    @Data
    public static class Response{
        private Long questionNum;
        private OffsetDateTime timestamp;
        private String questionTitle;
        private String questionDetail;
        private Questions.QuestionType questionType;
        private Boolean questionState;
        private String questionImage;
        private UserDTO  userNum;

        private AnswerResponse answerResponse;

        @Data
        public static class AnswerResponse{
            private Long answerNum;
            private OffsetDateTime timestamp;
            private String answerDetail;
        }
    }

    @Data
    public static class UserDTO {
        private Long userNum;
        private String userId;
        private String userName;
        private String userEmail;
    }
}
