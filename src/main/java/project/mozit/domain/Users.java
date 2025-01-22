package project.mozit.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_num")
    private Enterprises enterpriseNum;

    @OneToMany(mappedBy = "userNum", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Edits> edits;


    @Enumerated(EnumType.STRING)
    @Column(name = "user_sub")
    private SubType userSub;

    public enum SubType{
        Basic,
        Pro,
        Premium
    }
}
