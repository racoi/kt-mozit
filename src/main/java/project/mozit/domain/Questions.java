package project.mozit.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Data
public class Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionNum;

    @CreationTimestamp
    @Column(name = "timestamp")
    private OffsetDateTime timestamp;

    @Column(name = "question_title", nullable = false)
    private String questionTitle;

    @Column(name = "question_detail", nullable = false)
    private String questionDetail;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type")
    private QuestionType questionType;

    @Column(name = "question_state", nullable = false)
    private Boolean questionState = false;

    @Column(name = "question_image", length = 1000)
    private String questionImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_num")
    private Users userNum;

    public enum QuestionType{
        SERVICE,
        ACCOUNT,
        GENERAL
    }
}
