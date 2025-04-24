package com.thesys.titan.config;

import com.thesys.titan.jwt.LoginFilter;
import com.thesys.titan.jwt.JwtAuthenticationFilter;
import com.thesys.titan.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Slf4j
@Configuration
@EnableWebSecurity
// @RequiredArgsConstructor
public class SecurityConfig {
	// @Value("${spring.cfs.origin-url}")
	// private String originUrl;
	// @Value("${spring.cfs.origin-mng-url}")
	// private String originMngUrl;

	// private final AuthenticationConfiguration authenticationConfiguration;
	// private final JwtTokenProvider jwtTokenProvider;

	@Value("${spring.profiles.active:default}")
	private String activeProfile;

	public SecurityConfig(AuthenticationConfiguration authenticationConfiguration) { // , JwtTokenProvider
																						// jwtTokenProvider) {
		// this.authenticationConfiguration = authenticationConfiguration;
		// this.jwtTokenProvider = jwtTokenProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
				.cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
					CorsConfiguration config = new CorsConfiguration();
					config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
					config.setAllowedMethods(Collections.singletonList("*"));
					config.setAllowCredentials(true);
					config.setAllowedHeaders(Collections.singletonList("*"));
					config.setMaxAge(3600L);
					config.setExposedHeaders(Collections.singletonList("Authorization"));
					return config;
				}))
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				// .oauth2Login(oauth2 -> oauth2.loginPage("/login").userInfoEndpoint(endpoint
				// ->
				// endpoint.userService(customeOAuth2UserService)).successHandler(oAuth2SuccessHandler)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.httpBasic(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						// .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers("/swagger-ui/**").permitAll()
						.requestMatchers("/api-docs/**").permitAll()
						.requestMatchers("/api/**").permitAll()
						.requestMatchers("/login", "/join", "/").permitAll()
						.anyRequest().authenticated())
				// .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
				// LoginFilter.class)
				// .addFilterAt(new
				// LoginFilter(authenticationManager(authenticationConfiguration),jwtTokenProvider),
				// UsernamePasswordAuthenticationFilter.class)
				.headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
						.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Origin", "*")));
		// .headers(header -> header.addHeaderWriter(new
		// StaticHeadersWriter("Access-Control-Allow-Origin", "*")));

		// if (activeProfile.equals("dev")) {
		// http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
		// LoginFilter.class)
		// .addFilterAt(new
		// LoginFilter(authenticationManager(authenticationConfiguration),
		// jwtTokenProvider),
		// UsernamePasswordAuthenticationFilter.class);
		// }

		return http.build();
	}

	// @Bean
	// public SecurityFilterChain resouces(HttpSecurity http) throws Exception {
	// return http
	// .authorizeHttpRequests(httpReq -> httpReq
	// .requestMatchers("/js/**", "/css/**", "/images/**").permitAll())
	// .build();
	// }

}