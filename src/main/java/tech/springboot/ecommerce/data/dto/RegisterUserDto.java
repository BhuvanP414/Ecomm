package tech.springboot.ecommerce.data.dto;

import lombok.Data;

@Data
public class RegisterUserDto {
    private String name;
    private String username;
    private String password;
    private String passwordRe;
}
