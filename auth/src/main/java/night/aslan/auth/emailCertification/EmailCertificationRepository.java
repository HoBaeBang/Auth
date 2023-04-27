package night.aslan.auth.emailCertification;

import night.aslan.auth.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailCertificationRepository extends JpaRepository<EmailCertificationEntity,Long> {

}
