package project.mozit.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Notices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeNum;

    @CreationTimestamp
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "notice_title", nullable = false)
    private String noticeTitle;

    @Column(name = "notice_detail", nullable = false)
    private String noticeDetail;
}
