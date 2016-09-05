package com.justjournal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService justJournalUserDetailsService;

    @Override
    	public void configure(WebSecurity web) throws Exception {
    		// Spring Security ignores request to static resources such as CSS or JS files.
    		web
    				.ignoring()
    				.antMatchers("/static/**")
                    .antMatchers("/components/**")
                    .antMatchers("/images/**")
                    .antMatchers("/scripts/**")
                    .antMatchers("/software/**")
                    .antMatchers("/views/**");
    	}


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/home").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login/authenticate")
                .defaultSuccessUrl("/postlogin")
                .permitAll()
                .and()
            .logout()
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(justJournalUserDetailsService).passwordEncoder(new ShaPasswordEncoder());
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return justJournalUserDetailsService;
    }
}
