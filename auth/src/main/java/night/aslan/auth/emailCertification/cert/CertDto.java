package night.aslan.auth.emailCertification.cert;

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
