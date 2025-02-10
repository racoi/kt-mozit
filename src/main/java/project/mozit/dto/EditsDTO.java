package project.mozit.dto;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

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
    private OffsetDateTime timestamp;
    private Long userNum;
    private Boolean hasDownload;

}
