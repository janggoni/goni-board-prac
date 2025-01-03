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
        try {
            for (int i = 0; i < EXECUTE_COUNT; i++) {
                final int index = i;  // 람다에서 사용하기 위한 final 변수
                executorService.submit(() -> {
                    try {
                        insert(index);
                        latch.countDown();
                        System.out.println("Inserted batch " + index + ", remaining: " + latch.getCount());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            latch.await();
        } finally {
            executorService.shutdown();
        }
    }

    void insert(int batchIndex) {
        transactionTemplate.executeWithoutResult(status -> {
            for (int i = 0; i < BULK_INSERT_SIZE; i++) {
                Article article = Article.create(
                    snowflake.nextId(), 
                    "title-" + batchIndex + "-" + i, 
                    "content-" + batchIndex + "-" + i, 
                    1L, 
                    1L
                );
                entityManager.persist(article);
            }
            entityManager.flush();  // 명시적으로 flush 호출
        });
    }

}
