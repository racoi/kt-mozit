package project.mozit.dto;

import lombok.Data;

public class FaqsDTO {

    @Data
    public static class Post{
        private String faqQuestion;
        private String faqAnswer;
    }

    @Data
    public static class Patch{
        private String faqQuestion;
        private String faqAnswer;
    }

    @Data
    public static class Response{
        private Long faqNum;
        private String faqQuestion;
        private String faqAnswer;
    }
}
