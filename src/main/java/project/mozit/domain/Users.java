package project.mozit.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNum;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "user_pwd", nullable = false)
    private String userPwd;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "user_role", nullable = false)
    private String userRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_num")
    private Enterprises enterpriseNum;
}
