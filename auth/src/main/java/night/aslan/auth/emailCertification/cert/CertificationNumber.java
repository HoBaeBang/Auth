package night.aslan.auth.emailCertification.cert;

public interface CertificationNumber {
    public int createCertificationNumber();

    public boolean checkCertificationNumber(CertDto certDto);

}
