package project.mozit.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Enterprises {

    @Id
    private Long enterpriseNum;

    @Column(name = "enterprise_name", nullable = false)
    private String enterpriseName;
}
