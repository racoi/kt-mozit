package project.mozit.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EditsDTO {
    private Long editNum;
    private String editTitle;
    private String thumbnail;
    private LocalDateTime timestamp;
    private Long userNum;
    private Boolean hasDownload;
}
