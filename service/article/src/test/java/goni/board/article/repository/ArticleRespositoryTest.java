package goni.board.article.repository;


import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import goni.board.article.entity.Article;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class ArticleRespositoryTest {

    @Autowired
    ArticleRepository articleRepository;

    @Test
    public void testFindAll() {
        List<Article> articles = articleRepository.findAll(1L, 149997L, 30L);
        log.info("articles: {}", articles.size());
        for (Article article : articles) {
            log.info("article: {}", article);
        }

    }

    @Test
    void testCount() {
        Long count = articleRepository.count(1L, 30L);
        log.info("count: {}", count);
    }

    @Test
    void findInfiniteScrollTEST(){
        List<Article> articles = articleRepository.findAllInfiniteScroll(1L, 30L);
        for(Article article : articles){
            log.info("article = {}" ,article.getArticleId());
        }

        Long lastArticleId = articles.getLast().getArticleId();
        List<Article> articles2 = articleRepository.findAllInfiniteScroll(1L, 30L, lastArticleId);
        for(Article article : articles2){
            log.info("article = {}" ,article.getArticleId());
        }
    }

}
