package night.aslan.auth.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import night.aslan.auth.Form.ResponseForm;
import night.aslan.auth.emailValidation.EmailValidation;
import night.aslan.auth.member.Dto.MemberLoginDto;
import night.aslan.auth.member.Dto.MemberSignUpDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    /**
     * 회원가입
     * @param memberSignUpDto
     * @return
     */
    public String signUp(MemberSignUpDto memberSignUpDto){

        if (EmailValidation.isValid(memberSignUpDto.getMemberId())) {
            log.info(memberSignUpDto.getMemberPwd());
            String encodePwd = passwordEncoder.encode(memberSignUpDto.getMemberPwd());
            memberSignUpDto.setMemberPwd(encodePwd);
            MemberEntity memberEntity = new MemberEntity();
            memberEntity.signUoDtoToEntity(memberSignUpDto);
            memberRepository.save(memberEntity);
            return "회원 가입 성공";
        } else {
            return "이메일 형식의 아이디를 입력해 주세요";
        }
    }

    public ResponseForm login(MemberLoginDto dto){
        if (!EmailValidation.isValid(dto.getMemberId())){
            return ResponseForm.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .result("")
                    .comment("잘못된 요청입니다.")
                    .build();
        }else {
            //queryDsl사용해서 만들기

            return ResponseForm.builder()
                    .httpStatus(HttpStatus.OK)
                    .result("")
                    .comment("로그인을 환영합니다.")
                    .build();
        }
    }

    @Override       //security 때문
    public MemberEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        //MemberEntity 정보 조회
        Optional<MemberEntity> memberEntity = memberRepository.findByMemberId(username);


        if(memberEntity.isPresent()) {      //해당하는 값이 있다면
            MemberEntity memberEntityValue = memberEntity.get();

            MemberEntity member = MemberEntity.builder()
                    .memberSn(memberEntityValue.getMemberSn())
                    .memberId(memberEntityValue.getMemberId())
                    .memberPwd(memberEntityValue.getMemberPwd())
                    .memberName(memberEntityValue.getMemberName())
                    .memberAge(memberEntityValue.getMemberAge())
                    .memberAge(memberEntityValue.getMemberAge())
                    .memberRole(memberEntityValue.getMemberRole())
                    .memberToken(memberEntityValue.getMemberToken())
                    .memberCreateAt(memberEntityValue.getMemberCreateAt())
                    .memberUpdateAt(memberEntityValue.getMemberUpdateAt())
                    .build();

            log.info("member : {}", member);
            return member;
        }
        return null;
    }
}
