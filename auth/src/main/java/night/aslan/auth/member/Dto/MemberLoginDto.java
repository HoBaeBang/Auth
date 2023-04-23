package night.aslan.auth.member.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class MemberLoginDto {

    private String memberId;
    private String memberPwd;
}
