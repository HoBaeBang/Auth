package night.aslan.auth.emailCertification.cert;

import lombok.RequiredArgsConstructor;
import night.aslan.auth.emailCertification.EmailCertificationRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.Random;

@Repository
@RequiredArgsConstructor
public class CertificationNumberImpl implements CertificationNumber {

    private final EmailCertificationRepositoryCustom emailCertificationRepositoryCustom;
    @Override
    public int createCertificationNumber() {
        Random random = new Random();
        int certNumber = random.nextInt(999999 - 100000 + 1) + 100000;
        return certNumber;
    }

    @Override
    public boolean checkCertificationNumber(String email) {
        return false;
    }
}
