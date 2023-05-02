package night.aslan.auth.api.v1.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import night.aslan.auth.Form.ResponseForm;
import night.aslan.auth.api.v1.member.Dto.MemberLoginDto;
import night.aslan.auth.api.v1.member.Dto.MemberSignUpDto;
import night.aslan.auth.api.v1.member.querydslMember.MemberCustomRepository;
import night.aslan.auth.api.v1.email.EmailUtils;
import night.aslan.auth.api.v1.email.EmailValidation;
import night.aslan.auth.api.v1.email.emailCertification.EmailCertificationEntity;
import night.aslan.auth.api.v1.email.emailCertification.EmailCertificationImplCustom;
import night.aslan.auth.api.v1.email.emailCertification.EmailCertificationRepository;
import night.aslan.auth.api.v1.email.emailCertification.CertDto.CertDto;
import night.aslan.auth.jwt.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberCustomRepository memberCustomRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailUtils emailUtils;
    private final EmailCertificationImplCustom emailCertificationImplCustom;
    private final EmailCertificationRepository emailCertificationRepository;


    /**
     * 회원가입
     * @param memberSignUpDto
     * @return
     */
    public String signUp(MemberSignUpDto memberSignUpDto){

        if (EmailValidation.isValid(memberSignUpDto.getMemberId())) {   //이메일 형식 검사
            Optional<MemberEntity> member = memberRepository.findByMemberId(memberSignUpDto.getMemberId());
            if (member.isPresent()){    //일치하는 정보가 존재하는지 확인
                return "중복된 회원입니다.";
            }else {
                log.info(memberSignUpDto.getMemberPwd());   //로그
                String encodePwd = passwordEncoder.encode(memberSignUpDto.getMemberPwd());  //비밀번호 암호화
                memberSignUpDto.setMemberPwd(encodePwd);        //암호화한 비밀번호 주입
                MemberEntity memberEntity = new MemberEntity();     //회원 객체 만듬
                memberEntity.signUoDtoToEntity(memberSignUpDto);    //dto 객체를 entity객체로 변환
                memberRepository.save(memberEntity);                //entity 저장
                return "회원 가입 성공";
            }
        } else {
            return "이메일 형식의 아이디를 입력해 주세요";
        }
    }

    /**
     * 로그인
     * @param dto
     * @return
     */
    public ResponseForm login(MemberLoginDto dto){
        ResponseForm responseForm = new ResponseForm();
        //이메일 형식 검사
        if (!EmailValidation.isValid(dto.getMemberId())){
            responseForm.setHttpStatus(HttpStatus.BAD_REQUEST);
            responseForm.setResult("");
            responseForm.setComment("잘못된 요청입니다.");
        }else { //이메일 형식

            Optional<MemberEntity> member = memberRepository.findByMemberId(dto.getMemberId()); // DB 조회 select * from member_manage where member_id = 'dto.getMemberId()';

            //회원 아이디 찾기
            if (member.isEmpty()){
                responseForm.setHttpStatus(HttpStatus.BAD_REQUEST);
                responseForm.setResult("");
                responseForm.setComment("일치하는 회원정보가 없습니다.");
            }else {
                //회원 정보가 일치하는 정보 찾기 - queryDsl
                // -> 전체 로직을 생각해서 추후 성능개선 필요
                // -> 불필요한 쿼리 삭제
//                Optional<MemberEntity> member = memberCustomRepository.findLoginMember(dto);
                //Todo 로직 개선 필요
                if (passwordCheck(dto.getMemberPwd(),member.get().memberPwd)) {
                    String token = jwtTokenProvider.createToken(member.get().memberId);
                    member.get().setMemberToken(token);
                    responseForm.setHttpStatus(HttpStatus.OK);
                    responseForm.setResult(member.get());
                    responseForm.setComment("로그인을 환영합니다.");
                    responseForm.setAccessToken(token);
                } else {
                    responseForm.setHttpStatus(HttpStatus.BAD_REQUEST);
                    responseForm.setResult("");
                    responseForm.setComment("비밀번호가 일치하지 않습니다.");
                }
            }
        }
        return responseForm;
    }

    /**
     * 인증번호 발송 서비스
     * @param certDto
     * @return
     */
    public ResponseForm sendCertNumber(CertDto certDto) {
        ResponseForm response = new ResponseForm();
        try {
            int certCode = emailUtils.createCertificationNumber();
            EmailCertificationEntity emailCertificationEntity = new EmailCertificationEntity();
            emailCertificationEntity.setEmailCertificationEmail(certDto.getMemberId());
            emailCertificationEntity.setEmailCertificationNumber(certCode);
            emailCertificationEntity.setEmailCertCreatedAt(LocalDateTime.now());
            emailCertificationEntity.setEmailCertificationEnabled(false);
            emailCertificationRepository.save(emailCertificationEntity);
            String certEmailForm = certEmailFormat1 + String.valueOf(certCode) + certEmailFormat2;
            emailUtils.sendMail(certDto.getMemberId(), "이메일 인증 번호입니다.", htmlform1+certEmailForm+htmlform2);
            response.setHttpStatus(HttpStatus.OK);
            response.setComment("인증 번호가 발송되었습니다. 인증을 진행해 주세요");
        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setComment("인증 메일 발송에 실패하였습니다. 다시 시도해 주세요");
        }
        return response;
    }

    /**
     * 인증 번호 확인
     * @param certDto
     * @return
     */
    public ResponseForm checkCertificationNumber(CertDto certDto) {
        ResponseForm result = new ResponseForm();
        if (emailUtils.checkCertificationNumber(certDto)) {
            emailCertificationImplCustom.accessEmailCertification(certDto.getMemberId());
            result.setHttpStatus(HttpStatus.OK);
            result.setResult(true);
            result.setComment("인증에 성공하였습니다.");
        } else {
            result.setHttpStatus(HttpStatus.BAD_REQUEST);
            result.setResult(false);
            result.setComment("인증번호가 올바르지 않습니다.");
        }
        return result;
    }

    /**
     * 비밀번호 확인 메서드
     * @param dtoPwd
     * @param memberPwd
     * @return
     */
    public boolean passwordCheck(String dtoPwd,String memberPwd) {  //암호화된 비밀번호 일치 여부 확인
        return passwordEncoder.matches(dtoPwd, memberPwd);
    }






//--------------------------------------------------------------------------------------------------------------------------------
    //TOdo 해당 내용 따로 알아보기 - security 내용
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

    public static String certEmailFormat1 = "";
    public static String certEmailFormat2 = "";

    private static final String htmlform1 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
            "    <title>Demystifying Email Design</title>\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
            "</head>\n" +
            "<body style=\"margin: 0; padding: 0;\">\n" +
            "<table align=\"center\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"50%\">\n" +
            "    <tr>\n" +
            "        <td align=\"center\" bgcolor=\"#70bbd9\" style=\"width: 600px; height:300px;\">\n" +
            "            <img src=\"./img/c&pimg.png\" style=\"width:100%; height: 100%\" />\n" +
            "        </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td bgcolor=\"#ffffff\" style=\"padding: 40px 30px 40px 30px;\">\n" +
            "            <table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
            "                <tr>\n" +
            "                    <td width=\"260\" valign=\"top\">\n" +
            "                        <table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
            "                            <tr>\n" +
            "                                <td alt=\"\" width=\"100%\" height=\"140\" style=\"display: block;\">\n" +
            "                                    아래의 인증코드를 입력하여 본인인증을 완료해 주세요\n" +
            "                                </td>\n" +
            "                            </tr>\n" +
            "                            <tr>\n" +
            "                                <td style=\"padding: 25px 0 0 0;\">";

    private static final String htmlform2 = "</td>\n" +
            "                            </tr>\n" +
            "                        </table>\n" +
            "                    </td>\n" +
            "                </tr>\n" +
            "            </table>\n" +
            "        </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td bgcolor=\"#ee4c50\" style=\"padding: 30px 30px 30px 30px;\">\n" +
            "            <table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
            "                <tr></tr>\n" +
            "                    <td align=\"right\">\n" +
            "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "                            <tr>\n" +
            "                                <td>\n" +
            "                                    <a href=\"http://www.twitter.com/\">\n" +
            "                                        <img src=\"images/tw.gif\" alt=\"Twitter\" width=\"38\" height=\"38\" style=\"display: block;\" border=\"0\" />\n" +
            "                                    </a>\n" +
            "                                </td>\n" +
            "                                <td style=\"font-size: 0; line-height: 0;\" width=\"20\">&nbsp;</td>\n" +
            "                            </tr>\n" +
            "                        </table>\n" +
            "                    </td>\n" +
            "                    <td width=\"75%\">\n" +
            "                        &reg; Someone, somewhere 2013<br/>\n" +
            "                        Unsubscribe to this newsletter instantly\n" +
            "                    </td>\n" +
            "                    </td>\n" +
            "                </tr>\n" +
            "            </table>\n" +
            "        </td>\n" +
            "\n" +
            "    </tr>\n" +
            "</table>\n" +
            "</body>\n" +
            "</html>";
}
