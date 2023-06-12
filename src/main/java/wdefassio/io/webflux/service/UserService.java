package wdefassio.io.webflux.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import wdefassio.io.webflux.entity.User;
import wdefassio.io.webflux.mapper.UserMapper;
import wdefassio.io.webflux.model.request.UserRequest;
import wdefassio.io.webflux.repository.UserRepository;
import wdefassio.io.webflux.service.exception.ObjectNotFoundException;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    public Mono<User> save(final UserRequest request) {
        User user = mapper.toEntity(request);
        log.info("created user : {}", user);
        return userRepository.save(user);
    }

    public Mono<User> findById(final String id) {
        return userRepository.findById(id).switchIfEmpty(Mono.error(
                new ObjectNotFoundException(format("User not found for Id: %s", id))));
    }


}
