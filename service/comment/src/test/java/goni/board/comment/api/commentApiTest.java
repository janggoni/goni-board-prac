package goni.board.comment.api;

import goni.board.comment.service.request.CommentCreateRequest;
import goni.board.comment.service.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class commentApiTest {

    RestClient restClient = RestClient.create("http://localhost:9001");

    @Test
    void create() {
        CommentResponse response1 = create(new CommentCreateRequest(1L, "my comment1", null, 1L));
        CommentResponse response2 = create(new CommentCreateRequest(1L, "my comment2", response1.getCommentId(), 1L));
        CommentResponse response3 = create(new CommentCreateRequest(1L, "my comment3", response1.getCommentId(), 1L));

        System.out.println("commentId=%s".formatted(response1.getCommentId()));
        System.out.println("\tcommentId=%s".formatted(response2.getCommentId()));
        System.out.println("\tcommentId=%s".formatted(response3.getCommentId()));
    }

    CommentResponse createComment(CommentCreateRequest request) {
        return restClient.post()
               .uri("/v1/comments")
               .body(request)
               .retrieve()
               .body(CommentResponse.class);
    }


    @Test
    void read(){
        CommentResponse response = restClient.get()
                .uri("/v1/comments/{commentId}", 1L)
                .retrieve()
                .body(CommentResponse.class);
        System.out.println("response = " + response);
    }

    @Test
    void delete() {
        restClient.delete()
               .uri("/v1/comments/{commentId}", 1L)
               .retrieve();
    }

    @Getter
    @AllArgsConstructor
    private Long articleId;
    private String content;
    private Long parentCommentId;
    private Long writerId;
}
