package night.aslan.auth.member;

import lombok.*;
import night.aslan.auth.member.Dto.MemberSignUpDto;
import night.aslan.auth.role.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "member_manage")
public class MemberEntity implements UserDetails {
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

    @Column(name = "role")
    String memberRole;

    @Column(name = "token")
    String memberToken;

    @Column(name = "create_at")
    LocalDateTime memberCreateAt;

    @Column(name = "update_at")
    LocalDateTime memberUpdateAt;

    public void signUoDtoToEntity(MemberSignUpDto dto) {
        this.setMemberId(dto.getMemberId());
        this.setMemberPwd(dto.getMemberPwd());
        this.setMemberName(dto.getMemberName());
        this.setMemberAge(dto.getMemberAge());
        this.setMemberGender(dto.getMemberGender());
        this.setMemberRole(Role.User.getValue());
        this.setMemberCreateAt(LocalDateTime.now());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : memberRole.split(",")) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.memberPwd;
    }

    @Override
    public String getUsername() {
        return this.memberName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
