package night.aslan.auth.emailCertification;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import night.aslan.auth.emailCertification.cert.CertDto;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmailCertificationImplCustom implements EmailCertificationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<EmailCertificationEntity> findEmailCertById(Long id) {
        QEmailCertificationEntity emailCert = QEmailCertificationEntity.emailCertificationEntity;
        EmailCertificationEntity emailCertEntity = jpaQueryFactory.selectFrom(emailCert)
                .where(emailCert.emailCertificationId.eq(id)).fetchOne();
        return Optional.ofNullable(emailCertEntity);
    }

    @Override
    public Optional<EmailCertificationEntity> findCertificateByEmail(String email) {
        QEmailCertificationEntity emailCert = QEmailCertificationEntity.emailCertificationEntity;
        EmailCertificationEntity emailCertEntity = jpaQueryFactory.selectFrom(emailCert)
                .where(emailCert.emailCertificationEmail.eq(email)).fetchOne();
        return Optional.ofNullable(emailCertEntity);
    }




}
