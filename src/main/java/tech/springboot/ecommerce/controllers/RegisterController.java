package tech.springboot.ecommerce.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import tech.springboot.ecommerce.data.domain.UserEntity;
import tech.springboot.ecommerce.data.domain.UserRole;
import tech.springboot.ecommerce.data.dto.RegisterUserDto;
import tech.springboot.ecommerce.service.UserService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Controller
@RequiredArgsConstructor
public class RegisterController {
    private final UserService userService;

    @GetMapping("/register.html")
    public String renderRegistrationPage(Model model) {
        model.addAttribute("user", new RegisterUserDto());
        return "register.html";
    }

    @Transactional
    @PostMapping("/register.html")
    public String submitRegistration(@ModelAttribute("user") RegisterUserDto user, Model model) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (!user.getPassword().equals(user.getPasswordRe())) {
            model.addAttribute("error", "Provided passwords do not match");
            model.addAttribute("user", user);
            return "register.html";
        }

        UserEntity newUser = userService.registerUser(user, UserRole.CUST);
        if (newUser == null) {
            model.addAttribute("error", "User with the specified username already exists");
            model.addAttribute("user", user);
            return "register.html";
        }
        return "redirect:login.html";
    }
}
