package night.aslan.auth.member;

import lombok.RequiredArgsConstructor;
import night.aslan.auth.emailValidation.EmailValidation;
import night.aslan.auth.member.Dto.MemberSignUpDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    public String signUp(MemberSignUpDto memberSignUpDto){

        if (EmailValidation.isValid(memberSignUpDto.getMemberId())) {
            System.out.println(memberSignUpDto.getMemberPwd());
            String encodePwd = passwordEncoder.encode(memberSignUpDto.getMemberPwd());
            memberSignUpDto.setMemberPwd(encodePwd);
            MemberEntity memberEntity = new MemberEntity();
            memberEntity.toEntity(memberSignUpDto);
            System.out.println(memberEntity.getMemberPwd());
            memberRepository.save(memberEntity);
            return "회원 가입 성공";
        } else {
            return "이메일 형식의 아이디를 입력해 주세요";
        }
    }

}
