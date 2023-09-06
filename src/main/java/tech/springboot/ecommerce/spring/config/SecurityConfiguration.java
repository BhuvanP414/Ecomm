package tech.springboot.ecommerce.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import tech.springboot.ecommerce.service.authentication.UserLoginProvider;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain clinicFilterChain(HttpSecurity http,
                                                 UserLoginProvider userLoginProvider) throws Exception {
        http.cors().and()
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers("/admin/**", "/private/**", "/buy.html").authenticated()
                                .anyRequest().permitAll()
                                .and().authenticationProvider(userLoginProvider))
                .authenticationProvider(userLoginProvider)
                .formLogin(form -> form.loginPage("/login.html").permitAll())
                .logout(logout -> logout.permitAll());

        return http.build();
    }
}
