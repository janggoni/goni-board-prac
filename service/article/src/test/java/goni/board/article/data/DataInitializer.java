package goni.board.article.data;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import goni.board.article.entity.Article;
import goni.board.common.snowflake.Snowflake;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest
public class DataInitializer {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    TransactionTemplate transactionTemplate;
    Snowflake snowflake = new Snowflake();
    CountDownLatch latch = new CountDownLatch(EXECUTE_COUNT);

    static final int EXECUTE_COUNT = 6000;
    static final int BULK_INSERT_SIZE = 2000;

    @Test
    void init() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < EXECUTE_COUNT; i++) {
            executorService.submit(() -> {
               
                insert();
                latch.countDown();
                System.out.println("latch = " + latch.getCount());
               
            });
        }
        latch.await();
        executorService.shutdown();
    }

    void insert() {
        transactionTemplate.executeWithoutResult(status -> {
            for (int i = 0; i < BULK_INSERT_SIZE; i++) {
                Article article = Article.create(snowflake.nextId(), "titie" + i, "content" +i, 1L, 1L);
                entityManager.persist(article);
            }
        });
    }

}
