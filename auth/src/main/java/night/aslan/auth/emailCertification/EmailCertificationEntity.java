package night.aslan.auth.emailCertification;

import lombok.*;
import night.aslan.auth.member.MemberEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "email_cert")
public class EmailCertificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "email_cert_id")
    private Long emailCertificationId;

    @OneToOne(mappedBy = "emailCertificationEntity")
    private MemberEntity memberEntity;

    @Column(name = "email_cert_number")
    int emailCertificationNumber;

    @Column(name = "email_cert_enabled")
    boolean emailCerrificationEnabled;

    @Column(name = "email_cert_created_at")
    LocalDateTime emailCertCreatedAt;

    @Column(name = "email_cert_updated_at")
    LocalDateTime emailCertUpdatedAt;
}
