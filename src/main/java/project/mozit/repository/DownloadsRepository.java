package project.mozit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mozit.domain.Downloads;

@Repository
public interface DownloadsRepository extends JpaRepository<Downloads, Long> {
}