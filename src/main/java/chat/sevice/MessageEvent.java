package chat.sevice;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class MessageEvent extends ApplicationEvent {

    private String nick;
    private String message;

    public MessageEvent(Object source, String nick, String message) {
        super(source);
        this.nick = nick;
        this.message = message;
    }
}
