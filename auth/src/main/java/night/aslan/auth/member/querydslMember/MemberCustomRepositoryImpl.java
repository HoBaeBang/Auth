package night.aslan.auth.member.querydslMember;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import night.aslan.auth.member.Dto.MemberLoginDto;
import night.aslan.auth.member.MemberEntity;
import night.aslan.auth.member.QMemberEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Optional<MemberEntity> findLoginMember(MemberLoginDto memberLoginDto) {
        QMemberEntity memberEntity = QMemberEntity.memberEntity;
        MemberEntity result = jpaQueryFactory.selectFrom(memberEntity)
                .where(memberEntity.memberId.eq(memberLoginDto.getMemberId()), memberEntity.memberPwd.eq(passwordEncoder.encode(memberLoginDto.getMemberPwd())))
                .fetchOne();
        return Optional.ofNullable(result);
    }

}
