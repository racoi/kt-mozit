package project.mozit.dto;

import lombok.Data;

public class UsersDto {

    @Data
    public static class Post{
        private String userId;
        private String userName;
        private String userPwd;
        private String userEmail;
        private String enterpriseNum;
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
        private String enterpriseNum;
    }
}
