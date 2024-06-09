//package ru.mts.account.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import ru.mts.account.repository.UserRepository;
//
//@Configuration
//@RequiredArgsConstructor
//public class AppConfig {
//
//    private final UserRepository userRepository;
//
//    @Bean
//    UserDetailsService userDetailsService() {
//        return phone -> userRepository.findByPhone(phone)
//                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с номером телефона " + phone + " не найден"));
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    @Bean
//    AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//
//        authProvider.setUserDetailsService(userDetailsService());
//
//        return authProvider;
//    }
//}
