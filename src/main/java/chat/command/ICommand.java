package chat.command;

/**
 * @author Alexander Diachenko
 */
public interface ICommand {

    boolean check(final String command);

    String execute();
}
