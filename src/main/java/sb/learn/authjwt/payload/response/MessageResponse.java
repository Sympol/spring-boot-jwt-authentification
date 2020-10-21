package sb.learn.authjwt.payload.response;

/**
 * @author Symplice BONI
 * project auth-jwt
 * date 21/10/2020
 */
public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
