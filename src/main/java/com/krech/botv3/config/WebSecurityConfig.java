package com.krech.botv3.config;

import com.krech.botv3.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {



    private static final String USERS_API = "/users/**";
    private static final String AUTH_API = "/auth/**";
    private static final String WORDS_API = "/words/**";
    private static final String FILE_API = "/files/**";

    private final JwtFilter jwtFilter;

    @Autowired
    public WebSecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_API).permitAll()
                .antMatchers(USERS_API).hasAnyRole(Role.ADMIN.toString())
                .antMatchers(WORDS_API).hasAnyRole(Role.ADMIN.toString(), Role.USER.toString())
                .antMatchers(FILE_API).hasAnyRole(Role.ADMIN.toString())
                .and()
                .csrf().disable()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }


}
