package night.aslan.auth.emailCertification.cert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import night.aslan.auth.emailCertification.EmailCertificationEntity;
import night.aslan.auth.emailCertification.EmailCertificationRepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Random;

@Repository
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class CertificationNumberImpl implements CertificationNumber {

    private final EmailCertificationRepositoryCustom emailCertificationRepositoryCustom;
    @Override
    public int createCertificationNumber() {
        Random random = new Random();
        int certNumber = random.nextInt(999999 - 100000 + 1) + 100000;
        return certNumber;
    }

    @Override
    public boolean checkCertificationNumber(CertDto certDto) {
        EmailCertificationEntity entity = emailCertificationRepositoryCustom.findCertificateByEmail(certDto.getMemberId()).get();
        log.info("Checking certification number {}",entity);
        if (entity.getEmailCertificationNumber() == certDto.getCertNumber()) {
            return true;
        } else {
            return false;
        }
    }
}
