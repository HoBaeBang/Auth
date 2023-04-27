package night.aslan.auth.api.v1.member.Dto;

import lombok.*;

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
