package night.aslan.auth.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import night.aslan.auth.Form.ResponseForm;
import night.aslan.auth.email.EmailUtils;
import night.aslan.auth.email.EmailValidation;
import night.aslan.auth.emailCertification.EmailCertificationEntity;
import night.aslan.auth.emailCertification.EmailCertificationImplCustom;
import night.aslan.auth.emailCertification.EmailCertificationRepository;
import night.aslan.auth.emailCertification.cert.CertDto;
import night.aslan.auth.emailCertification.cert.CertificationNumber;
import night.aslan.auth.emailCertification.cert.CertificationNumberImpl;
import night.aslan.auth.jwt.JwtTokenProvider;
import night.aslan.auth.member.Dto.MemberLoginDto;
import night.aslan.auth.member.Dto.MemberSignUpDto;
import night.aslan.auth.member.querydslMember.MemberCustomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final CertificationNumber certificationNumber;
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
                    responseForm.setHttpStatus(HttpStatus.OK);
                    responseForm.setResult(member.get());
                    responseForm.setComment("로그인을 환영합니다.");
                    responseForm.setAccessToken(jwtTokenProvider.createToken(member.get().memberId));
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
    public String sendCertNumber(CertDto certDto) {
        try {
            int certCode = certificationNumber.createCertificationNumber();
            EmailCertificationEntity emailCertificationEntity = new EmailCertificationEntity();
            emailCertificationEntity.setEmailCertificationEmail(certDto.getMemberId());
            emailCertificationEntity.setEmailCertificationNumber(certCode);
            emailCertificationEntity.setEmailCertCreatedAt(LocalDateTime.now());
            emailCertificationEntity.setEmailCertificationEnabled(false);
            emailCertificationRepository.save(emailCertificationEntity);
            emailUtils.sendMail(certDto.getMemberId(), "이메일 인증 번호입니다.", String.valueOf(certCode));
            return "인증 번호가 발송되었습니다. 인증을 진행해 주세요";

        } catch (Exception e) {
            return "인증 메일 발송에 실패하였습니다. 다시 시도해 주세요";
        }
    }

    /**
     * 인증 번호 확인
     * @param certDto
     * @return
     */
    public String checkCertificationNumber(CertDto certDto) {
        if (certificationNumber.checkCertificationNumber(certDto)) {
            emailCertificationImplCustom.accessEmailCertification(certDto.getMemberId());
            return "인증에 성공하였습니다.";
        } else {
            return "인증번호가 올바르지 않습니다.";
        }
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
}
