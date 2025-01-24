package goni.board.articleread.service.event.handler;

import goni.board.common.event.Event;
import goni.board.common.event.EventPayload;

public interface EventHandler<T extends EventPayload> {
    void handle(Event<T> event);
    boolean supports(Event<T> event);
}
