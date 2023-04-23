package night.aslan.auth.Form;

import lombok.*;
import org.springframework.http.HttpStatus;
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ResponseForm {
    private HttpStatus httpStatus;  //통신요청 상태 ok badrequest
    private Object result;          //결과값
    private String comment;         //코멘트 내가 무슨경우에서 발생했는지 알고싶어서 넣음
    private String accessToken;     //토큰
}
