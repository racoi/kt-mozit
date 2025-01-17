package project.mozit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mozit.domain.Edits;
import project.mozit.domain.Users;

import java.util.List;

@Repository
public interface EditsRepository extends JpaRepository<Edits, Long> {
    List<Edits> findByUserNum(Users userNum);
}
