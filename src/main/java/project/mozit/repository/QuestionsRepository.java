package project.mozit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mozit.domain.Questions;

import java.util.List;

@Repository
public interface QuestionsRepository extends JpaRepository<Questions, Long> {

    List<Questions> findByUserNum_UserId(String userId);
}
