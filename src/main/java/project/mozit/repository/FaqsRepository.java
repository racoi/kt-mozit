package project.mozit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mozit.domain.Faqs;

@Repository
public interface FaqsRepository extends JpaRepository<Faqs, Long> {
}
