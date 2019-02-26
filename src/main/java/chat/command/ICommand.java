package chat.command;

/**
 * @author Alexander Diachenko
 */
public interface ICommand {

    boolean canExecute(String command);

    String execute();
}
