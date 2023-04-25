package night.aslan.auth.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import night.aslan.auth.Form.ResponseForm;
import night.aslan.auth.email.EmailUtils;
import night.aslan.auth.member.Dto.MemberLoginDto;
import night.aslan.auth.member.Dto.MemberSignUpDto;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final EmailUtils emailUtils;

    @PostMapping("/login")
    public ResponseForm login(@RequestBody MemberLoginDto memberLoginDto) {
        return memberService.login(memberLoginDto);
    }

    @PostMapping("/signup")
    public String signup(@RequestBody MemberSignUpDto memberSignUpDto) {
        return memberService.signUp(memberSignUpDto);
    }

    @PostMapping("/mailtest")
    public Map<String, Object> sendMail(@RequestBody MemberLoginDto memberLoginDto) {
        log.info("id={}",memberLoginDto.getMemberId());
        return emailUtils.sendMail(memberLoginDto.getMemberId(), "emailTest 인증번호 발송", "test용 인증번호입니다. 1351531515151");
    }}
