package project.mozit.dto;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EditsDTO {
    private Long editNum;
    private String thumbnail;
    private LocalDateTime timestamp;
    private Long userNum;
}
