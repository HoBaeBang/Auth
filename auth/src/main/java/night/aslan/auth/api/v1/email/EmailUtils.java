package night.aslan.auth.api.v1.email;

import night.aslan.auth.api.v1.email.emailCertification.CertDto.CertDto;

import java.util.Map;

public interface EmailUtils {
    Map<String, Object> sendMail(String emailAddress, String subject, String body);
    int createCertificationNumber();
    boolean checkCertificationNumber(CertDto certDto);

}
