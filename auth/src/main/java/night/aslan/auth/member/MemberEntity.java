package night.aslan.auth.member;

import lombok.*;
import night.aslan.auth.member.Dto.MemberSignUpDto;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "member_manage")
public class MemberEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_sn")
    Long memberSn;

    @Column(name = "member_id")         //아이디는 이메일로 받기
    String memberId;

    @Column(name = "member_pwd")        //비밀번호 암호화 해서 저장할것
    String memberPwd;                   //bcrtpt 사용할것

    @Column(name = "member_name")       //회원 이름
    String memberName;

    @Column(name = "member_age")
    int memberAge;

    @Column(name = "member_gender")
    String memberGender;

    public void toEntity(MemberSignUpDto dto) {
        this.setMemberId(dto.getMemberId());
        this.setMemberPwd(dto.getMemberPwd());
        this.setMemberName(dto.getMemberName());
        this.setMemberAge(dto.getMemberAge());
        this.setMemberGender(dto.getMemberGender());
    }
}
