package night.aslan.auth.api.v1.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import night.aslan.auth.Form.ResponseForm;
import night.aslan.auth.api.v1.member.Dto.MemberLoginDto;
import night.aslan.auth.api.v1.member.Dto.MemberSignUpDto;
import night.aslan.auth.api.v1.email.EmailUtils;
import night.aslan.auth.api.v1.email.emailCertification.CertDto.CertDto;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final EmailUtils emailUtils;


    //인증번호 발송
    @PostMapping("/create/cert-number")
    public String sendCertNumber(@RequestBody CertDto certDto) {
        return memberService.sendCertNumber(certDto);
    }

    //인증번호 인증
    @PostMapping("/check/cert-number")
    public String checkCodeNumber(@RequestBody CertDto certDto) {
        return memberService.checkCertificationNumber(certDto);
    }
    //로그인
    @PostMapping("/login")
    public ResponseForm login(@RequestBody MemberLoginDto memberLoginDto) {
        return memberService.login(memberLoginDto);
    }
    //회원가입
    @PostMapping("/signup")
    public String signup(@RequestBody MemberSignUpDto memberSignUpDto) {
        return memberService.signUp(memberSignUpDto);
    }
    //메일 전송 테스트
    @PostMapping("/mailtest")
    public Map<String, Object> sendMail(@RequestBody MemberLoginDto memberLoginDto) {
        log.info("id={}", memberLoginDto.getMemberId());

        return emailUtils.sendMail(memberLoginDto.getMemberId(), "emailTest 인증번호 발송", "test용 인증번호입니다. 1351531515151");
    }

}

