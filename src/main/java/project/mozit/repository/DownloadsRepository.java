package project.mozit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mozit.domain.Downloads;
import project.mozit.domain.Edits;

@Repository
public interface DownloadsRepository extends JpaRepository<Downloads, Long> {
    boolean existsByEditNum(Edits editNum); // editNum에 대한 다운로드 존재 여부 확인
}
