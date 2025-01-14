package project.mozit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mozit.domain.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    boolean existsByUserId(String userid);
    Users findByUserId(String username);
}
