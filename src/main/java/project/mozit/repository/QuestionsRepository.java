package project.mozit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mozit.domain.Questions;

@Repository
public interface QuestionsRepository extends JpaRepository<Questions, Long> {
}
