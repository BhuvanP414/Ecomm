package tech.springboot.ecommerce.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test.html")
    public String publicEndpoint() {
        return "I can access this";
    }

    @GetMapping("/admin/test.html")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String protectedAdminEndpoint() {
        return "This will be displayed just for admin";
    }

    @GetMapping("/private/test.html")
    public String protectedEndpoint() {
        return "This will be displayed for any logged in user";
    }
}
