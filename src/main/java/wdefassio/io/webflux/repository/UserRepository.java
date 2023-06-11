package wdefassio.io.webflux.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import wdefassio.io.webflux.entity.User;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    public Mono<User> save(final User user) {
        return mongoTemplate.save(user);
    }

    public Mono<User> findById(final String id) {
        return mongoTemplate.findById(id, User.class);
    }

}
