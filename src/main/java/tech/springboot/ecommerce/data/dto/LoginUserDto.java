package tech.springboot.ecommerce.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDto {
    private String username;
    private String password;

    private boolean error = false;
}
