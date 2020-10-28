package com.kyberpunk.iot.watering.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig {

    @Configuration
    @EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
    @ConditionalOnProperty(value = "watering.security", havingValue = "none", matchIfMissing = true)
    public static class DefaultSecurityConfig {

    }

    @EnableAutoConfiguration
    @EnableWebSecurity
    @ConditionalOnProperty(value = "watering.security", havingValue = "oauth2")
    public static class Oauth2SecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/error", "/login**").permitAll()
                    .anyRequest().authenticated()
                    .and().logout().logoutSuccessUrl("/login").permitAll()
                    .and().oauth2Login();
        }
    }
}
