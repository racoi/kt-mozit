package project.mozit.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Enterprises {

    @Id
    private Integer EnterpriseNum;

    @Column(name = "enterprise_name", nullable = false)
    private String EnterpriseName;

    @Column(name = "enterprise_addr", nullable = false)
    private String EnterpriseAddr;

    @Column(name = "enterprise_call", nullable = false)
    private String EnterpriseCall;
}
