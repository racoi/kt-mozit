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

    @Column(name = "enterprise_addr", nullable = false)
    private String enterpriseAddr;

    @Column(name = "enterprise_call", nullable = false)
    private String enterpriseCall;
}
