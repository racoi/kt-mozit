package project.mozit.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Enterprises {

    @Id
    private Long enterpriseNum;

    @Column(name = "enterprise_name", nullable = false)
    private String enterpriseName;

    @OneToMany(mappedBy = "enterpriseNum", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Users> users;
}
