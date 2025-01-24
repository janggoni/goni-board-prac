package goni.board.articleread.service.event.handler;

import goni.board.articleread.repository.ArticleIdListRepository;
import goni.board.articleread.repository.ArticleQueryModelRepository;
import goni.board.articleread.repository.BoardArticleCountRepository;
import goni.board.common.event.Event;
import goni.board.common.event.EventType;
import goni.board.common.event.payload.ArticleDeletedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {
    private final ArticleIdListRepository articleIdListRepository;
    private final ArticleQueryModelRepository articleQueryModelRepository;
    private final BoardArticleCountRepository boardArticleCountRepository;

    @Override
    public void handle(Event<ArticleDeletedEventPayload> event) {
        // 이시점에 사용자는 게시글 목록 조회할수 있음. 순서를 잘맞춰서 짜야함
        ArticleDeletedEventPayload payload = event.getPayload();
        articleIdListRepository.delete(payload.getBoardId(), payload.getArticleId());

        articleQueryModelRepository.delete(payload.getArticleId());
        boardArticleCountRepository.createOrUpdate(payload.getBoardId(), payload.getBoardArticleCount());
    }

    @Override
    public boolean supports(Event<ArticleDeletedEventPayload> event) {
        return EventType.ARTICLE_DELETED == event.getType();
    }
}
