package project.mozit.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Data
public class Answers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerNum;

    @OneToOne
    @JoinColumn(name = "question_num")
    private Questions questionNum;

    @CreationTimestamp
    @Column(name = "timestamp")
    private OffsetDateTime timestamp;

    @Column(name = "answer_detail", nullable = false)
    private String answerDetail;
}
