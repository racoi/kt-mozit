package project.mozit.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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
    private LocalDateTime timestamp;

    @Column(name = "answer_detail", nullable = false)
    private String answerDetail;
}
