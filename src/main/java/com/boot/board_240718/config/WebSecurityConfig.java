package com.boot.board_240718.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
    @EnableWebSecurity
    public class WebSecurityConfig {
        @Autowired
        private DataSource dataSource;
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//           csrf 공격 해제
            http.csrf(CsrfConfigurer::disable);
            http
                    .authorizeHttpRequests((requests) -> requests
//                            .requestMatchers("/").permitAll()
//                            .requestMatchers("/", "/css/**").permitAll()
//                            .requestMatchers("/", "/css/**", "/images/**").permitAll()
//                            .requestMatchers("/", "/css/**", "/images/**", "/account/register").permitAll()
                            .requestMatchers("/", "/css/**", "/images/**", "/account/register", "/api/**").permitAll()
                            .anyRequest().authenticated()
                    )
                    .formLogin((form) -> form
//                            .loginPage("/login")
                            .loginPage("/account/login")
                            .permitAll()
                    )
                    .logout((logout) -> logout.permitAll());

            return http.build();
        }

        @Bean
        public UserDetailsService userDetailsService() {
            UserDetails user =
                    User.withDefaultPasswordEncoder()
                            .username("user")
                            .password("password")
                            .roles("USER")
                            .build();

            return new InMemoryUserDetailsManager(user);
        }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
//              .passwordEncoder : 메소드
                .passwordEncoder(passwordEncoder())
//                .usersByUsernameQuery("select username,password,enabled "
                .usersByUsernameQuery("select username, password, enable "
                        + "from user "
                        + "where username = ?")
                .authoritiesByUsernameQuery("SELECT u.username, r.name " +
                        "FROM user_role ur " +
                        "INNER JOIN USER u " +
                        "ON ur.user_id = u.id " +
                        "INNER JOIN role r " +
                        "ON ur.role_id = r.id " +
                        "WHERE u.username = ?");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();//이 방식으로 객체를 생성하면서 암호화
    }
}

