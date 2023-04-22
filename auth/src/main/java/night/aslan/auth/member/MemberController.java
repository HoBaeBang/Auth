package night.aslan.auth.member;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import night.aslan.auth.member.Dto.MemberSignUpDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/login")
    public String login(){
        return "ok";
    }

    @PostMapping("/signup")
    public String signup(@RequestBody MemberSignUpDto memberSignUpDto) {
        return memberService.signUp(memberSignUpDto);
    }

}
