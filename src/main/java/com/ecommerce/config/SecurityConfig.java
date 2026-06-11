package com.ecommerce.config;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.ecommerce.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	// Injected from PasswordConfig — no circular reference
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	// DaoAuthenticationProvider wires userDetails + passwordEncoder cleanly
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(
			org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration config)
			throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authenticationProvider(authenticationProvider())
				.authorizeHttpRequests(
						auth -> auth.requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
								.requestMatchers("/", "/shop/**", "/product/**", "/search", "/register", "/login",
										"/track", "/faq", "/shipping-policy", "/returns", "/contact")
								.permitAll().requestMatchers("/track").permitAll().requestMatchers("/delivery/**")
								.hasRole("DELIVERY").requestMatchers("/faq", "/shipping-policy", "/returns", "/contact")
								.permitAll().requestMatchers("/admin/**").hasRole("ADMIN")
								.requestMatchers("/orders/*/bill").hasAnyRole("CUSTOMER", "ADMIN")
								.requestMatchers("/cart/**", "/checkout/**", "/orders/**", "/profile/**")
								.hasRole("CUSTOMER").anyRequest().authenticated())

				.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login")
						.successHandler((request, response, authentication) -> {

							boolean isAdmin = authentication.getAuthorities().stream()
									.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

							boolean isDelivery = authentication.getAuthorities().stream()
									.anyMatch(a -> a.getAuthority().equals("ROLE_DELIVERY"));

							if (isAdmin) {
								response.sendRedirect("/admin");
							} else if (isDelivery) {
								response.sendRedirect("/delivery/dashboard");
							} else {
								response.sendRedirect("/");
							}
						}).failureUrl("/login?error=true").permitAll())

				.logout(logout -> logout.logoutUrl("/logout")

						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/login?logout=true").invalidateHttpSession(true).clearAuthentication(true)
						.deleteCookies("JSESSIONID").permitAll())

				.rememberMe(remember -> remember.key("shopwave-secret-key").tokenValiditySeconds(7 * 24 * 60 * 60)
						.userDetailsService(userDetailsService))

				.headers(headers -> headers.contentSecurityPolicy(csp -> csp
						.policyDirectives("default-src 'self'; " + "img-src 'self' data: https://images.unsplash.com "
								+ "https://via.placeholder.com https://cdnjs.cloudflare.com; "
								+ "script-src 'self' https://cdnjs.cloudflare.com; "
								+ "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com "
								+ "https://cdnjs.cloudflare.com; " + "font-src 'self' https://fonts.gstatic.com "
								+ "https://cdnjs.cloudflare.com")));

		return http.build();
	}
}