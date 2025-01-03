package goni.board.article.api;

import org.junit.jupiter.api.Test;

import org.springframework.web.client.RestClient;

import goni.board.article.service.response.ArticlePageResponse;
import goni.board.article.service.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ArticleApiTest {
    RestClient restClient = RestClient.create("http://localhost:9000");

   @Test
    void createTest() {
        ArticleResponse response = create(new ArticleCreateRequest(
                "hi", "my content", 1L, 1L
        ));
        System.out.println("response = " + response);
    }

    ArticleResponse create(ArticleCreateRequest request) {
        return restClient.post()
                .uri("/v1/articles")
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void readTest() {
        ArticleResponse response = read(121530268440289280L);
        System.out.println("response = " + response);
    }

    ArticleResponse read(Long articleId) {
        return restClient.get()
                .uri("/v1/articles/{articleId}", articleId)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void updateTest() {
        update(121530268440289280L);
        ArticleResponse response = read(121530268440289280L);
        System.out.println("response = " + response);
    }

    void update(Long articleId) {
        restClient.put()
                .uri("/v1/articles/{articleId}", articleId)
                .body(new ArticleUpdateRequest("hi 2", "my content 22"))
                .retrieve();
    }

    @Test
    void deleteTest() {
        restClient.delete()
                .uri("/v1/articles/{articleId}", 121530268440289280L)
                .retrieve();
    }

    @Test
    void readAllTest() {
        try {
            ArticlePageResponse response = restClient.get()
                    .uri("/v1/articles?boardId=1&pageSize=30&page=50000")
                    .retrieve()
                    .body(ArticlePageResponse.class);

            System.out.println("Response received: " + response);  // 전체 응답 출력
            
            if (response != null && response.getArticles() != null) {
                System.out.println("Total articles: " + response.getArticles().size());
                for (ArticleResponse article : response.getArticles()) {
                    System.out.println("articleId = " + article.getArticleId());
                }
            } else {
                System.out.println("No articles found or response is null");
            }
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // @Test
    // void readAllInfiniteScrollTest() {
    //     List<ArticleResponse> articles1 = restClient.get()
    //             .uri("/v1/articles/infinite-scroll?boardId=1&pageSize=5")
    //             .retrieve()
    //             .body(new ParameterizedTypeReference<List<ArticleResponse>>() {
    //             });

    //     System.out.println("firstPage");
    //     for (ArticleResponse articleResponse : articles1) {
    //         System.out.println("articleResponse.getArticleId() = " + articleResponse.getArticleId());
    //     }

    //     Long lastArticleId = articles1.getLast().getArticleId();
    //     List<ArticleResponse> articles2 = restClient.get()
    //             .uri("/v1/articles/infinite-scroll?boardId=1&pageSize=5&lastArticleId=%s".formatted(lastArticleId))
    //             .retrieve()
    //             .body(new ParameterizedTypeReference<List<ArticleResponse>>() {
    //             });

    //     System.out.println("secondPage");
    //     for (ArticleResponse articleResponse : articles2) {
    //         System.out.println("articleResponse.getArticleId() = " + articleResponse.getArticleId());
    //     }
    // }

    // @Test
    // void countTest() {
    //     ArticleResponse response = create(new ArticleCreateRequest("hi", "content", 1L, 2L));

    //     Long count1 = restClient.get()
    //             .uri("/v1/articles/boards/{boardId}/count", 2L)
    //             .retrieve()
    //             .body(Long.class);
    //     System.out.println("count1 = " + count1); // 1

    //     restClient.delete()
    //             .uri("/v1/articles/{articleId}", response.getArticleId())
    //             .retrieve();

    //     Long count2 = restClient.get()
    //             .uri("/v1/articles/boards/{boardId}/count", 2L)
    //             .retrieve()
    //             .body(Long.class);
    //     System.out.println("count2 = " + count2); // 0
    // }


    @Getter
    @AllArgsConstructor
    static class ArticleCreateRequest {
        private String title;
        private String content;
        private Long writerId;
        private Long boardId;
    }

    @Getter
    @AllArgsConstructor
    static class ArticleUpdateRequest {
        private String title;
        private String content;
    }



}
