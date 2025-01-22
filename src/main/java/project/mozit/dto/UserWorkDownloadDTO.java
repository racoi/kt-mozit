package project.mozit.dto;

import lombok.Data;

public class UserWorkDownloadDTO {

    @Data
    public static class Response{
        private Long userNum;
        private String username;
        private String enterpriseName;
        private Long workCount;
        private Long downloadCount;
    }
}
