package night.aslan.auth.emailCertification;

import night.aslan.auth.member.Dto.MemberLoginDto;
import night.aslan.auth.member.MemberEntity;

import java.util.Optional;

public interface EmailCertificationRepository {
    Optional<EmailCertificationEntity> findEmailCertById(Long id);
}
