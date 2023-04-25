package night.aslan.auth.emailCertification;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmailCertificationImpl implements EmailCertificationRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<EmailCertificationEntity> findEmailCertById(Long id) {
        QEmailCertificationEntity emailCert = QEmailCertificationEntity.emailCertificationEntity;
        EmailCertificationEntity emailCertEntity = jpaQueryFactory.selectFrom(emailCert)
                .where(emailCert.emailCertificationId.eq(id)).fetchOne();
        return Optional.ofNullable(emailCertEntity);
    }
}
