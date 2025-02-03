package project.mozit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.mozit.domain.Edits;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadsDTO {
    private Boolean faceMosaic;
    private String hazardousList;
    private String personalList;
    private Edits editNum;
}
