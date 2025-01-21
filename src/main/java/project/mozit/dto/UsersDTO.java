package project.mozit.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
<<<<<<< HEAD
=======
        private String enterpriseAddr;
        private String enterpriseCall;
>>>>>>> d39e813934a412cd21484fe569c5905d24bf8fe5
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

        private Long enterpriseNum;
        private String enterpriseName;
<<<<<<< HEAD
=======
        private String enterpriseAddr;
        private String enterpriseCall;
>>>>>>> d39e813934a412cd21484fe569c5905d24bf8fe5
    }
}
