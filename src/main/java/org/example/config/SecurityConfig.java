package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author LuoYunXiao
 * @since 2023/10/13 10:47
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .formLogin(configurer-> {
                    try {
                        configurer
                                .and()
                                .authorizeRequests().mvcMatchers("**").permitAll()
                                .and()
                                .authorizeRequests().anyRequest().authenticated();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .csrf().disable().build();
    }

}
