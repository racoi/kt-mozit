package project.mozit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mozit.domain.Notices;

@Repository
public interface NoticesRepository extends JpaRepository<Notices, Long> {
}
