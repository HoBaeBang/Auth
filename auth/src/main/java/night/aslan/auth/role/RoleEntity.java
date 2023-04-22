package night.aslan.auth.role;

import javax.persistence.*;

@Entity
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_sn")
    Long roleSn;

    @Column(name = "role")

}
