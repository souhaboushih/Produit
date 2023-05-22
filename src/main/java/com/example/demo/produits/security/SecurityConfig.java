package com.example.demo.produits.security;

import com.example.demo.produits.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
@Configuration
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
    @Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // TODO Auto-generated method stub
    PasswordEncoder passwordEncoder = passwordEncoder ();
    auth.inMemoryAuthentication().withUser("admin")
            .password(passwordEncoder.encode("123")).roles("ADMIN");
    auth.inMemoryAuthentication().withUser("Najla")
            .password(passwordEncoder.encode("123")).roles("AGENT","USER");
    auth.inMemoryAuthentication().withUser("user1")
            .password(passwordEncoder.encode("123")).roles("USER");
}
@Override
protected void configure(HttpSecurity http) throws Exception {
    // TODO Auto-generated method stub
    http.authorizeRequests().antMatchers("/search").hasAnyRole("USER", "AGENT", "ADMIN");
    http.authorizeRequests().antMatchers("/showCreate","/saveProduit").hasAnyRole("ADMIN","AGENT");
    http.authorizeRequests().antMatchers("/ListeProduits")
            .hasAnyRole("ADMIN","AGENT","USER");
    http.authorizeRequests()
            .antMatchers("/supprimerProduit","/modifierProduit","/updateProduit")
            .hasAnyRole("ADMIN");
    http.authorizeRequests().anyRequest().authenticated();
    http.formLogin();
    http.exceptionHandling().accessDeniedPage("/accessDenied");
}
@Bean
public PasswordEncoder passwordEncoder () {
    return new BCryptPasswordEncoder();
}

    /*
    public static final String AUTHORITIES_CLAIM_NAME = "roles";
    @Autowired
    UserService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authenticationProvider(getProvider())
                .formLogin()
                .loginProcessingUrl("/login")
                .successHandler(new AuthentificationLoginSuccessHandler())
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(new AuthentificationLogoutSuccessHandler())
                .invalidateHttpSession(true)
                .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/logout").permitAll()
                .anyRequest().authenticated();
    }
    private class AuthentificationLoginSuccessHandler extends
            SimpleUrlAuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                            Authentication authentication) throws IOException, ServletException {
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
    private class AuthentificationLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                    Authentication authentication) throws IOException, ServletException {
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Bean
    public AuthenticationProvider getProvider() {
        AppAuthProvide provider = new AppAuthProvide();
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new AccessDeniedHandlerImpl();
    }*/
        }
