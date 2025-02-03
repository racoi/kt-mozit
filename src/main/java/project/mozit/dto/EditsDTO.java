package project.mozit.dto;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EditsDTO {
    private Long editNum;
    // Getter와 Setter
    @Setter
    @Getter
    private String editTitle;
    private String thumbnail;
    private LocalDateTime timestamp;
    private Long userNum;
    private Boolean hasDownload;

}
