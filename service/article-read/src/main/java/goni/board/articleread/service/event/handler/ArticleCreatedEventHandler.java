package goni.board.articleread.service.event.handler;

import goni.board.articleread.repository.ArticleIdListRepository;
import goni.board.articleread.repository.ArticleQueryModel;
import goni.board.articleread.repository.ArticleQueryModelRepository;
import goni.board.articleread.repository.BoardArticleCountRepository;
import goni.board.common.event.Event;
import goni.board.common.event.EventType;
import goni.board.common.event.payload.ArticleCreatedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ArticleCreatedEventHandler implements EventHandler<ArticleCreatedEventPayload> {
    private final ArticleIdListRepository articleIdListRepository;
    private final ArticleQueryModelRepository articleQueryModelRepository;
    private final BoardArticleCountRepository boardArticleCountRepository;

    @Override
    public void handle(Event<ArticleCreatedEventPayload> event) {
        ArticleCreatedEventPayload payload = event.getPayload();
        // 게시글이 먼저 성생되게 되면 사용자가 이시점에 조회할때에는 목록에는 노출되고 아티클 쿼리 모델에는 생성안되기 때메생성 순서
        articleQueryModelRepository.create(
                ArticleQueryModel.create(payload),
                Duration.ofDays(1)
        );
        articleIdListRepository.add(payload.getBoardId(), payload.getArticleId(), 1000L);
        boardArticleCountRepository.createOrUpdate(payload.getBoardId(), payload.getBoardArticleCount());
    }

    @Override
    public boolean supports(Event<ArticleCreatedEventPayload> event) {
        return EventType.ARTICLE_CREATED == event.getType();
    }
}
