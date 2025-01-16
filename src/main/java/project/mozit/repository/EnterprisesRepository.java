package project.mozit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mozit.domain.Enterprises;

import java.util.Optional;

@Repository
public interface EnterprisesRepository extends JpaRepository<Enterprises, Long> {
    Optional<Enterprises> findByEnterpriseNum(Long EnterpriseNum);
}
