package wdefassio.io.webflux.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import wdefassio.io.webflux.entity.User;
import wdefassio.io.webflux.mapper.UserMapper;
import wdefassio.io.webflux.model.request.UserRequest;
import wdefassio.io.webflux.repository.UserRepository;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
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
    void save() {
        UserRequest request = new UserRequest("valdir", "valdir@email.com", "1234");
        User user = User.builder().build();
        when(mapper.toEntity(any(UserRequest.class))).thenReturn(user);
        when(repository.save(any(User.class))).thenReturn(Mono.just(User.builder().build()));
        Mono<User> save = userService.save(request);
        StepVerifier.create(save).expectNextMatches(Objects::nonNull).expectComplete().verify();

        Mockito.verify(repository, times(1)).save(any(User.class));
    }
}