package project.mozit.dto;

import lombok.Data;
import project.mozit.domain.Enterprises;

public class UsersDTO {

    @Data
    public static class Post{
        private String userId;
        private String userName;
        private String userPwd;
        private String userEmail;

        private Long enterpriseNum;
        private String enterpriseName;
        private String enterpriseAddr;
        private String enterpriseCall;
    }

    @Data
    public static class Patch{
        private String userPwd;
    }

    @Data
    public static class Response{
        private Long userNum;
        private String userId;
        private String userName;
        private String userEmail;
        private Enterprises enterpriseNum;
    }
}
