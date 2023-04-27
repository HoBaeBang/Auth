package night.aslan.auth.api.v1.member.querydslMember;

import night.aslan.auth.api.v1.member.Dto.MemberLoginDto;
import night.aslan.auth.api.v1.member.MemberEntity;

import java.util.Optional;

public interface MemberCustomRepository {
    Optional<MemberEntity> findLoginMember(MemberLoginDto memberLoginDto);
}
