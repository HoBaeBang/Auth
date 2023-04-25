package night.aslan.auth.email;

import java.util.Map;

public interface EmailUtils {
    Map<String, Object> sendMail(String emailAddress, String subject, String body);
}
