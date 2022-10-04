package com.example.thymeleafdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class DemoSecurityConfig {

	@Autowired
	@Qualifier("securityDataSource")
	private DataSource securityDataSource;

	@Bean
	public UserDetailsManager userDetailsManager() {
		return new JdbcUserDetailsManager(securityDataSource);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		return http
				.authorizeRequests(configurer ->
						configurer
//							.anyRequest().authenticated()
							.antMatchers("/employees/showForm*").hasAnyRole("MANAGER", "ADMIN")
							.antMatchers("/employees/save*").hasAnyRole("MANAGER", "ADMIN")
							.antMatchers("/employees/delete").hasRole("ADMIN")
							.antMatchers("/employees/**").hasRole("EMPLOYEE")
							.antMatchers("/resources/**").permitAll())

				.formLogin(configurer ->
						configurer
								.loginPage("/showMyLoginPage")
								.loginProcessingUrl("/authenticateTheUser")
								.permitAll())

				.logout(configurer ->
						configurer
								.permitAll())

				.exceptionHandling(configurer ->
						configurer
								.accessDeniedPage("/access-denied"))

				.build();
	}
}