package night.aslan.auth.member.Dto;

import lombok.*;
import night.aslan.auth.member.MemberEntity;
import org.springframework.security.crypto.password.PasswordEncoder;


import javax.persistence.Column;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class MemberSignUpDto {

    public String memberId;
    public String memberPwd;
    public String memberName;
    public int memberAge;
    public String memberGender;

//    public MemberEntity toEntity() {
//
//        return MemberEntity.builder()
//                .memberId(this.memberId)
//                .memberPwd(passwordEncoder.encode(this.memberPwd))
//                .memberName(this.memberName)
//                .memberAge(this.memberAge)
//                .memberGender(this.memberGender)
//                .build();
//    }
}
