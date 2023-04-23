package night.aslan.auth.member.querydslMember;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import night.aslan.auth.member.Dto.MemberLoginDto;
import night.aslan.auth.member.MemberEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

public interface MemberCustomRepository {
    Optional<MemberEntity> findLoginMember(MemberLoginDto memberLoginDto);
}
