package com.cmpe281.project1;

import com.cmpe281.project1.authorization.JwtTokenFilter;
import com.cmpe281.project1.authorization.JwtTokenProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SpringBootApplication
public class Project1Application {

    public static void main(String[] args) {
        SpringApplication.run(Project1Application.class, args);
    }

    @Configuration
    @EnableWebSecurity
    public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        private JwtTokenProvider tokenProvider = new JwtTokenProvider();

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/signup").permitAll()
                    .anyRequest().hasAuthority("myAuthority");
            http.addFilterBefore(new JwtTokenFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
        }
    }
}