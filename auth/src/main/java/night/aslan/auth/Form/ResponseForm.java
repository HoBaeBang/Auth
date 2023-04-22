package night.aslan.auth.Form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ResponseForm {
    private HttpStatus httpStatus;
    private Object result;
    private String comment;
}
