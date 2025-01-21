package project.mozit.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import project.mozit.domain.Enterprises;
import project.mozit.domain.Users;

public class UsersDTO {

    @Data
    public static class Post{
        private String userId;
        private String userName;
        private String userPwd;
        private String userEmail;
        private Users.SubType subType;

        private Long enterpriseNum;
        private String enterpriseName;
    }

    @Data
    public static class Patch{
        private String userName;
        private String userEmail;
        private String userPwd;
    }

    @Data
    public static class Response{
        private Long userNum;
        private String userId;
        private String userName;
        private String userEmail;
        private Users.SubType subType;

        private Long enterpriseNum;
        private String enterpriseName;
    }
}
