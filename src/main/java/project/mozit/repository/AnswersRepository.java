package project.mozit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mozit.domain.Answers;
import project.mozit.domain.Questions;

import java.util.Optional;

@Repository
public interface AnswersRepository extends JpaRepository<Answers, Long> {

    Optional<Answers> findByQuestionNum(Questions question);
}
