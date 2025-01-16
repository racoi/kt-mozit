package project.mozit.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.mozit.domain.Users;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    boolean existsByUserId(String userid);
    boolean existsByUserEmail(String userid);
    Users findByUserId(String userid);
    Optional<Users>  findByUserEmail(String email);

    @Query("SELECT u.userId FROM Users u WHERE u.userEmail = :email")
    String findUserIdByUserEmail(@Param("email") String email);

}
