package wdefassio.io.webflux.controller;

import com.mongodb.reactivestreams.client.MongoClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import wdefassio.io.webflux.entity.User;
import wdefassio.io.webflux.mapper.UserMapper;
import wdefassio.io.webflux.model.request.UserRequest;
import wdefassio.io.webflux.model.response.UserResponse;
import wdefassio.io.webflux.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerImplTest {

    @Autowired
    private WebTestClient client;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper mapper;
    @MockBean
    private MongoClient mongoClient;

    private final String baseUri = "/api/v1/users";

    @Test
    @DisplayName("Test endpoint save with success")
    void saveWithSuccess() {

        UserRequest request = new UserRequest("valdir", "123456", "valdir@email.com");

        when(userService.save(any(UserRequest.class))).thenReturn(Mono.just(User.builder().build()));

        client.post().uri(baseUri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isCreated();

        Mockito.verify(userService, times(1)).save(any());
    }

    @Test
    @DisplayName("Test with bad request")
    void saveWithError() {

        UserRequest request = new UserRequest(" valdir ", "123456", "valdir@email.com");

        client.post().uri(baseUri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo(baseUri)
                .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.error").isEqualTo("Validation error")
                .jsonPath("$.errors[0].fieldName").isEqualTo("name")
                .jsonPath("$.errors[0].message").isEqualTo("field cannot start end finish with blank spaces");
    }

    @Test
    @DisplayName("find by id success")
    void find() {
        String id = "123456id";
        UserResponse userResponse = new UserResponse(id, "Valdir", "valdir@email.com", "123456");
        when(userService.findById(anyString())).thenReturn(Mono.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse);

        client.get().uri(baseUri + "/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id);

    }

    @Test
    @DisplayName("Test find all endpoint with success")
    void testFindAllWithSuccess() {

        String id = "123456id";
        UserResponse userResponse = new UserResponse(id, "Valdir", "valdir@email.com", "123456");
        when(userService.findAll()).thenReturn(Flux.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse);

        client.get().uri(baseUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(id);


    }

    @Test
    @DisplayName("Test update")
    void update() {
        String id = "123456id";
        UserResponse userResponse = new UserResponse(id, "Valdir", "valdir@email.com", "123456");
        UserRequest request = new UserRequest(" valdir ", "123456", "valdir@email.com");

        when(userService.update(anyString(), any(UserRequest.class))).thenReturn(Mono.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse);

        client.patch().uri(baseUri + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id);


    }

    @Test
    @DisplayName("delete test")
    void deleteTest() {

        String id = "123456id";
        when(userService.delete(anyString())).thenReturn(Mono.just(User.builder().build()));

        client.delete().uri(baseUri + "/" + id)
                .exchange()
                .expectStatus().isOk();

    }
}