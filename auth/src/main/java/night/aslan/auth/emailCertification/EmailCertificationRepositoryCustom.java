package night.aslan.auth.emailCertification;

import night.aslan.auth.emailCertification.cert.CertDto;

import java.util.Optional;

public interface EmailCertificationRepositoryCustom {
    Optional<EmailCertificationEntity> findEmailCertById(Long id);

    Optional<EmailCertificationEntity> findCertificateByEmail(String email);

    Long accessEmailCertification(String email);

}
