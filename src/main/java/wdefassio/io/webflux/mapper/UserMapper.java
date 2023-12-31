package wdefassio.io.webflux.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import wdefassio.io.webflux.entity.User;
import wdefassio.io.webflux.model.request.UserRequest;
import wdefassio.io.webflux.model.response.UserResponse;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = IGNORE, nullValueCheckStrategy = ALWAYS)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toEntity(final UserRequest userRequest);
    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequest request, @MappingTarget User entity);

    UserResponse toResponse(final User user);


}
