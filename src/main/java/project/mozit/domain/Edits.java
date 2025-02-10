package project.mozit.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Data
public class Edits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long editNum;

    @CreationTimestamp
    @Column(name = "timestamp")
    private OffsetDateTime timestamp;

    @Column(name = "edit_title")
    private String editTitle;

    @Column(name = "thumbnail", nullable = false, length = 1000)
    private String thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_num")
    private Users userNum;

    @OneToOne(mappedBy = "editNum", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Downloads downloads;
}
