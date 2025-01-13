package project.mozit.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Data
public class Downloads {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long downloadNum;

    @OneToOne
    @JoinColumn(name = "edit_num")
    private Edits editNum;

    @Column(name = "personal_list", nullable = false)
    private String personalList;

    @Column(name = "hazardous_list", nullable = false)
    private String hazardousList;

    @Column(name = "face_mosaic", nullable = false)
    @ColumnDefault("0")
    private Boolean faceMosaic;
}
