package chat.bot;

import chat.sevice.Bot;
import chat.util.Paths;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@Getter
@Setter
@Component
public abstract class AbstractBotStarter {

    public static PircBotX bot;
    @Autowired
    protected Bot listener;
    @Autowired
    protected Paths paths;

    public void start() {
        Thread thread = new Thread(() -> {
            configureBot();
            try {
                bot.startBot();
            } catch (IOException | IrcException exception) {
                log.error(exception.getMessage(), exception);
                throw new RuntimeException("Bot failed to start.\n " +
                        "Check properties in " + getBotProperties() + " " +
                        "and restart application.", exception);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public Bot getListener() {
        return listener;
    }

    protected abstract String getBotProperties();

    protected abstract void configureBot();
}
