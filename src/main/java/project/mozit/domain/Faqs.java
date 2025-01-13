package project.mozit.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Faqs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long faqNum;

    @Column(name = "faq_question", nullable = false)
    private String faqQuestion;

    @Column(name = "faq_answer", nullable = false)
    private String faqAnswer;
}
