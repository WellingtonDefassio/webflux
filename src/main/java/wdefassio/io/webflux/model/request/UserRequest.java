package wdefassio.io.webflux.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import wdefassio.io.webflux.validator.TrimString;

public record UserRequest(
        @NotBlank(message = "must not be null or empty")
        @Size(min = 3, max = 50, message = "size must be between 3 and 50 characters")
        @TrimString
        String name,
        @NotBlank(message = "must not be null or empty")
        @Size(min = 3, max = 50, message = "size must be between 3 and 50 characters")
        @TrimString
        String password,
        @Email(message = "invalid email")
        @NotBlank(message = "must not be null or empty")
        @TrimString
        String email

) {
}
