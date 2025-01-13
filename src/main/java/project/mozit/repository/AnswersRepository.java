package project.mozit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mozit.domain.Answers;

@Repository
public interface AnswersRepository extends JpaRepository<Answers, Long> {
}
