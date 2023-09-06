package tech.springboot.ecommerce.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tech.springboot.ecommerce.data.dto.LoginUserDto;

@Controller
public class LoginController {
    @GetMapping("/login.html")
    public String renderLogin(@RequestParam(value = "error", required = false) String error, Model model) {
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setError(error != null);

        model.addAttribute("credentials", loginUserDto);
        return "login.html";
    }
}
