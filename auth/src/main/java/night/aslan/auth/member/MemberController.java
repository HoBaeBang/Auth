package night.aslan.auth.member;

import lombok.RequiredArgsConstructor;
import night.aslan.auth.Form.ResponseForm;
import night.aslan.auth.member.Dto.MemberLoginDto;
import night.aslan.auth.member.Dto.MemberSignUpDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/login")
    public ResponseForm login(@RequestBody MemberLoginDto memberLoginDto) {
        return  memberService.login(memberLoginDto);
    }

    @PostMapping("/signup")
    public String signup(@RequestBody MemberSignUpDto memberSignUpDto) {
        return memberService.signUp(memberSignUpDto);
    }

}
