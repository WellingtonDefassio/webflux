package wdefassio.io.webflux.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import wdefassio.io.webflux.entity.User;
import wdefassio.io.webflux.model.request.UserRequest;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = IGNORE, nullValueCheckStrategy = ALWAYS)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toEntity(final UserRequest userRequest);

}
