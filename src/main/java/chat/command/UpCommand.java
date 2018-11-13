package chat.command;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author Alexander Diachenko
 */
public class UpCommand implements ICommand {

    private LocalDateTime start;

    public UpCommand(LocalDateTime start) {
        this.start = start;
    }

    @Override
    public boolean canExecute(String command) {
        return "!up".equalsIgnoreCase(command);
    }

    @Override
    public String execute() {
        Duration between = Duration.between(this.start, LocalDateTime.now());
        return String.format(
                "%02dh %02dm %02ds",
                between.toHours(),
                between.toMinutes(),
                between.toSeconds());
    }
}
