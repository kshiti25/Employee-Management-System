package com.csye6220.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
////@Configuration
////@EnableWebSecurity
////public class WebSecurityConfig {
////
////    @Bean
////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http
////            .csrf(csrf -> csrf.disable()) // Disable CSRF protection for simplicity (not recommended for production)
////            .formLogin(formLogin ->
////                formLogin
////                    .loginPage("/login") // Custom login page
////                    .defaultSuccessUrl("/index", true) // Redirect to homepage on success
////                    .permitAll()
////            )
////         
////            .authorizeHttpRequests(authorize ->
////                authorize
////                    .requestMatchers("/register").permitAll() // Allow access to registration page
////                    .anyRequest().authenticated() // Require authentication for all other requests
////            );
////
////        return http.build();
////    }
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
////    }
////}
//
//package com.csye6220.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable()) // Disable CSRF protection for simplicity (not recommended for production)
//            .formLogin(formLogin ->
//                formLogin
//                    .loginPage("/login") // Custom login page
//                    .usernameParameter("username")  // match with your form's username input field name
//                    .passwordParameter("password")  // match with your form's password input field name
//                    .defaultSuccessUrl("/index", true) // Redirect to homepage on success
//                    .permitAll()
//            )
//            .authorizeHttpRequests(authorize ->
//                authorize
//                    .requestMatchers("/register").permitAll() // Allow access to registration page
//                    .anyRequest().authenticated() // Require authentication for all other requests
//            );
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
//






import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .requestMatchers("/login", "/register").permitAll()
            .requestMatchers("/index").authenticated() 
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/index", true)
            .permitAll()
            .and()
            .logout()
            .logoutSuccessUrl("/login")
            .permitAll();

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider);
    }
}