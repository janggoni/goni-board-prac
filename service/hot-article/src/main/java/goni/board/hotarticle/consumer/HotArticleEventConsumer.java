package goni.board.hotarticle.consumer;

import goni.board.common.event.Event;
import goni.board.common.event.EventPayload;
import goni.board.common.event.EventType;
import goni.board.hotarticle.service.HotArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotArticleEventConsumer {
    private final HotArticleService hotArticleService;

    @KafkaListener(topics = {
            EventType.Topic.GONI_BOARD_ARTICLE,
            EventType.Topic.GONI_BOARD_COMMENT,
            EventType.Topic.GONI_BOARD_LIKE,
            EventType.Topic.GONI_BOARD_VIEW
    })
    public void listen(String message, Acknowledgment ack) {
        log.info("[HotArticleEventConsumer.listen] received message={}", message);
        Event<EventPayload> event = Event.fromJson(message);
        if (event != null) {
            hotArticleService.handleEvent(event);
        }
        ack.acknowledge();
    }
}
