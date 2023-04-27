package night.aslan.auth.api.v1.member.role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    Admin("ROLE_ADMIN"),
    User("Role_User");

    private String value;
}
