/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, Vit Holasek
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.kyberpunk.iot.watering.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * This class contains Spring Security configuration used for the application. These configurations are exclusive.
 * Configuration is chosen based on application properties.
 */
@Configuration
public class SecurityConfig {

    /**
     * This configuration disable Spring Security features. Configuration is applied when "watering.security" property
     * is set to "none" or if it is not set.
     */
    @Configuration
    @EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
    @ConditionalOnProperty(value = "watering.security", havingValue = "none", matchIfMissing = true)
    public static class DefaultSecurityConfig {

    }

    /**
     * This configuration enables and configures Spring Security OAuth2 client. OAuth endpoint is used to authenticate
     * all requests to watering service resources. Appropriate properties must be set in application properties.
     * See https://spring.io/guides/tutorials/spring-boot-oauth2/ for more information. Configuration is applied only
     * when "watering.security" property is set to "oauth2".
     * @see <a href="https://spring.io/guides/tutorials/spring-boot-oauth2/">https://spring.io/guides/tutorials/spring-boot-oauth2/</a>
     */
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
