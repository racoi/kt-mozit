package project.mozit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mozit.domain.Edits;

@Repository
public interface EditsRepository extends JpaRepository<Edits, Long> {
}
