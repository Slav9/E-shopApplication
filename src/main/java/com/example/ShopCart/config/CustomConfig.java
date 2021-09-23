// config for password encoder (moved here because of bean loading error)
package com.example.ShopCart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class CustomConfig {
    @Bean
    public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder(8);
}
}
