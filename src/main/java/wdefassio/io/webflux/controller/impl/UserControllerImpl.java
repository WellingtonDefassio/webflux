package wdefassio.io.webflux.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import wdefassio.io.webflux.controller.UserController;
import wdefassio.io.webflux.mapper.UserMapper;
import wdefassio.io.webflux.model.request.UserRequest;
import wdefassio.io.webflux.model.response.UserResponse;
import wdefassio.io.webflux.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserControllerImpl implements UserController {

    private final UserService userService;
    private final UserMapper mapper;

    @Override
    public ResponseEntity<Mono<Void>> save(UserRequest request) {
        log.info("accept de request : {}", request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.save(request)
                        .then());
    }

    @Override
    public ResponseEntity<Mono<UserResponse>> find(String id) {
        return ResponseEntity.ok().body(userService.findById(id).map(mapper::toResponse));
    }

    @Override
    public ResponseEntity<Flux<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll().map(mapper::toResponse));
    }

    @Override
    public ResponseEntity<Mono<UserResponse>> update(String id, UserRequest request) {
        return ResponseEntity.ok().body(userService.update(id,request).map(mapper::toResponse));
    }

    @Override
    public ResponseEntity<Mono<Void>> delete(String id) {
        return ResponseEntity.ok().body(userService.delete(id).then());
    }
}
