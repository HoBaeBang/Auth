package night.aslan.auth.api.v1.email;

import java.util.Map;

public interface EmailUtils {
    Map<String, Object> sendMail(String emailAddress, String subject, String body);
}
