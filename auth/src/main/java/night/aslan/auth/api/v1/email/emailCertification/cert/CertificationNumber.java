package night.aslan.auth.api.v1.email.emailCertification.cert;

public interface CertificationNumber {
    public int createCertificationNumber();

    public boolean checkCertificationNumber(CertDto certDto);

}
