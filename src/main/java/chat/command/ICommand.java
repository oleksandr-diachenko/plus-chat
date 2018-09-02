package chat.command;

/**
 * @author Alexander Diachenko
 */
public interface ICommand {

    boolean canExecute(final String command);

    String execute();
}
