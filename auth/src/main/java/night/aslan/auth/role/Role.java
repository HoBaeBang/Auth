package night.aslan.auth.role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    Admin("ROLE_ADMIN"),
    User("Role_User");

    private String value;
}
