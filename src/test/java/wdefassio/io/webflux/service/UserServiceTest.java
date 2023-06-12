package wdefassio.io.webflux.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import wdefassio.io.webflux.entity.User;
import wdefassio.io.webflux.mapper.UserMapper;
import wdefassio.io.webflux.model.request.UserRequest;
import wdefassio.io.webflux.repository.UserRepository;
import wdefassio.io.webflux.service.exception.ObjectNotFoundException;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService userService;


    @Test
    void testSave() {
        UserRequest request = new UserRequest("valdir", "valdir@email.com", "1234");
        User user = User.builder().build();
        when(mapper.toEntity(any(UserRequest.class))).thenReturn(user);
        when(repository.save(any(User.class))).thenReturn(Mono.just(User.builder().build()));
        Mono<User> save = userService.save(request);
        StepVerifier.create(save).expectNextMatches(Objects::nonNull).expectComplete().verify();

        Mockito.verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void testFindByIdTest() {
        when(repository.findById(anyString())).thenReturn(Mono.just(User.builder().build()));
        Mono<User> user = userService.findById("123");

        StepVerifier.create(user).expectNextMatches(Objects::nonNull).expectComplete().verify();

        Mockito.verify(repository, times(1)).findById(any(String.class));

    }

    @Test
    void findAllTest() {
        when(repository.findAll()).thenReturn(Flux.just(User.builder().build()));
        Flux<User> user = userService.findAll();

        StepVerifier.create(user).expectNextMatches(Objects::nonNull).expectComplete().verify();

        Mockito.verify(repository, times(1)).findAll();

    }

    @Test
    void deleteTest() {

        UserRequest request = new UserRequest("valdir", "valdir@email.com", "1234");
        User user = User.builder().build();
        when(mapper.toEntity(any(UserRequest.class), any(User.class))).thenReturn(user);
        when(repository.findById(anyString())).thenReturn(Mono.just(user));
        when(repository.save(any(User.class))).thenReturn(Mono.just(user));

        Mono<User> save = userService.update("123", request);
        StepVerifier.create(save).expectNextMatches(Objects::nonNull).expectComplete().verify();

        Mockito.verify(repository, times(1)).save(any(User.class));
        Mockito.verify(repository, times(1)).findById(anyString());

    }


    @Test
    void testDeleteTest() {
        User user = User.builder().build();

        when(repository.delete(anyString())).thenReturn(Mono.just(user));

        Mono<User> save = userService.delete("123");

        StepVerifier.create(save).expectNextMatches(Objects::nonNull).expectComplete().verify();
        Mockito.verify(repository, times(1)).delete(anyString());
    }

    @Test
    void handleNotFoundTest() {

        when(repository.findById(anyString())).thenReturn(Mono.empty());
        try {
            userService.findById("123");
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), ObjectNotFoundException.class);
        }
    }
}