package chat.bot;

import chat.sevice.Bot;

public interface Startable {

    void start();

    Bot getListener();
}
