package night.aslan.auth.member.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class MemberLoginDto {

    private String memberId;
    private String memberPwd;
}
