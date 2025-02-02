package project.mozit.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.mozit.domain.Users;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    boolean existsByUserId(String userid);
    boolean existsByUserEmail(String userid);
    Users findByUserId(String userid);
    Optional<Users>  findByUserEmail(String email);

    @Query("SELECT u.userId FROM Users u WHERE u.userEmail = :email")
    String findUserIdByUserEmail(@Param("email") String email);

    @Query(value = "SELECT u.user_num AS user_num, u.user_name, c.enterprise_name, " +
            "COUNT(DISTINCT e.edit_num) AS editCount, " +
            "COUNT(DISTINCT d.download_num) AS downloadCount " +
            "FROM users u " +
            "LEFT JOIN edits e ON u.user_num = e.user_num " +
            "LEFT JOIN downloads d ON e.edit_num = d.edit_num " +
            "LEFT JOIN enterprises c ON u.enterprise_num = c.enterprise_num " +
            "WHERE u.user_id != 'admin' " +
            "GROUP BY u.user_num, u.user_name, c.enterprise_name", nativeQuery = true)
    List<Object[]> findUserWorkDownloadNative();
}
