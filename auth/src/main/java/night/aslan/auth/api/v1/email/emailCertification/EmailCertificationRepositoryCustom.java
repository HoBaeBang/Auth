package night.aslan.auth.api.v1.email.emailCertification;

import java.util.Optional;

public interface EmailCertificationRepositoryCustom {
    Optional<EmailCertificationEntity> findEmailCertById(Long id);

    Optional<EmailCertificationEntity> findCertificateByEmail(String email);

    Long accessEmailCertification(String email);

}
