package goni.board.article.service;

import org.springframework.stereotype.Service;

import goni.board.common.snowflake.Snowflake;
import goni.board.article.entity.Article;
import goni.board.article.repository.ArticleRepository;
import goni.board.article.service.request.ArticleCreateRequest;
import goni.board.article.service.request.ArticleUpdateRequest;
import goni.board.article.service.response.ArticlePageResponse;
import goni.board.article.service.response.ArticleResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final Snowflake snowflake = new Snowflake();
    private final ArticleRepository articleRepository;

    @Transactional
    public ArticleResponse create(ArticleCreateRequest request) {
        Article article = articleRepository.save(
            Article.create(snowflake.nextId(), request.getTitle(), request.getContent(), request.getBoardId(), request.getWriterId())
            );
        return ArticleResponse.from(article);
    }

    @Transactional
     public ArticleResponse update(Long articleId, ArticleUpdateRequest request) {
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new RuntimeException("Article not found"));
        article.update(request.getTitle(), request.getContent());
        return ArticleResponse.from(article);
     }
    
     public ArticleResponse read(Long articleId) {
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new RuntimeException("Article not found"));
            System.out.println(article);
        return ArticleResponse.from(article);
     }

     @Transactional
     public void delete(Long articleId) {
        articleRepository.deleteById(articleId);
     }

     public ArticlePageResponse readAll(Long boardId, Long page, Long pageSize) {
        return ArticlePageResponse.of(
            articleRepository.findAll(boardId, (page -1) * pageSize, pageSize).stream()
            .map(ArticleResponse::from)
            .toList(),
            articleRepository.count(
                boardId, 
                PageLimitCalculator.calculatePageLimit(page,pageSize,10L))
        );
     }
}
