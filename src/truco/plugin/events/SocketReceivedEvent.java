/*

 */
package truco.plugin.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class SocketReceivedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final String message;
    private final String canal;

    public String getMessage() {
        return message;
    }

    public String getCanal() {
        return canal;
    }

    public SocketReceivedEvent(String message, String canal) {
        this.message = message;
        this.canal = canal;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
