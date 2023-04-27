package night.aslan.auth.api.v1.email.emailCertification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailCertificationRepository extends JpaRepository<EmailCertificationEntity,Long> {

}
