package night.aslan.auth.api.v1.email.emailCertification.CertDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CertDto {
    private String memberId;
    private int certNumber;
}
