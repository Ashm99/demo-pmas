package com.example.pmas.patientmedicineappointmentsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration{

    @Bean
    public  UserDetailsService userDetailsService(){
        return new PatientDetailsService();
        // Below data is for testing purposes
//        UserDetails user = User.withUsername("Ram").password("{noop}123").roles("USER").build();
//        UserDetails admin = User.withUsername("Raman").password("{noop}1234").roles("ADMIN").build();
//        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
//                .csrf(csrf -> csrf
//                        .ignoringRequestMatchers(
//                                "/web/appointments/doctors",
//                                "/web/appointments/checkSlots"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( // URL matching for allowed pages
                                "/web/patients/login",
                                "/web/patients/register",
                                "/web/patients/savePatient",
                                "/web/doctors/register",
                                "/web/doctors/saveDoctor",
                                "/doctors/getAll",
                                "/doctors/get/{id}",
                                "/css/**",
                                "/js/**"
                        ).permitAll()
                        .requestMatchers( // URL matching for allowed pages
                                HttpMethod.POST,
                                "/doctors/add",
                                "/doctors/addAll"
                        ).permitAll()
                        .requestMatchers( // URL matching for allowed pages
                                HttpMethod.PUT,
                                "/doctors/update/{id}"
                        ).permitAll()
                        .requestMatchers( // URL matching for allowed pages
                                HttpMethod.DELETE,
                                "/doctors/delete/{id}"
                        ).permitAll()
                        .anyRequest().authenticated()) // Stating that any other request should be authenticated
                .formLogin(form -> form
                        .loginPage("/web/patients/login")
                        // The controller method that renders the html page should have this endpoint of GET type
                        // Further the same html login page should make a POST request to this same url in order to authenticate/login
                        .defaultSuccessUrl("/web/patients/home", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout") // Sets the URL that triggers the logout action.
//                        .logoutSuccessUrl("/login?logout") // After logging out, users will be redirected to the login
//                        .logoutUrl("/web/patients/logout") // Sets the URL that triggers the logout action.
                        .logoutSuccessUrl("/web/patients/login?logout") // After logging out, users will be redirected to the login
                        // page with a logout parameter indicating successful logout.
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()); // Custom logout handling
        return http.build();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
}
