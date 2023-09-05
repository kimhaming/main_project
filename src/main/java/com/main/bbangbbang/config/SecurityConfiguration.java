package com.main.bbangbbang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.transaction.TransactionDefinition.withDefaults;

//
@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
//                .cors(withDefaults())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()  // 추가
//                .authenticationEntryPoint(new MemberAuthenticationEntryPoint())  // JWT Filter
//                .accessDeniedHandler(new MemberAccessDeniedHandler())            // 기본 403에러 말고 다른 처리 해주고 싶을 때
                .and()
//                .apply(new CustomFilterConfigurer())  //
//                .and()
                .authorizeHttpRequests(authorize -> authorize // url authorization 전체 추가
//                        .antMatchers(HttpMethod.POST, "/*/members").permitAll()    // OAuth 2로 로그인하므로 회원 정보 등록 필요 없음.
//                        .antMatchers(HttpMethod.PATCH, "/*/members/**").hasRole("USER") // OAuth 2로 로그인하므로 회원 정보 수정 필요 없음.
//                        .antMatchers(HttpMethod.GET, "/*/members").hasRole("ADMIN")  // OAuth 2로 로그인하므로 회원 정보 수정 필요 없음.
//                        .antMatchers(HttpMethod.GET, "/*/members/**").hasAnyRole("USER", "ADMIN")  // OAuth 2로 로그인하므로 회원 정보 수정 필요 없음.
//                        .antMatchers(HttpMethod.DELETE, "/*/members/**").hasRole("USER") // OAuth 2로 로그인하므로 회원 정보 수정 필요 없음.
                                .antMatchers(HttpMethod.POST, "/*/coffees").hasRole("ADMIN")
                                .antMatchers(HttpMethod.PATCH, "/*/coffees/**").hasRole("ADMIN")
                                .antMatchers(HttpMethod.GET, "/*/coffees/**").hasAnyRole("USER", "ADMIN")
                                .antMatchers(HttpMethod.GET, "/*/coffees").permitAll()
                                .antMatchers(HttpMethod.DELETE, "/*/coffees").hasRole("ADMIN")
                                .antMatchers(HttpMethod.POST, "/*/orders").hasRole("USER")
                                .antMatchers(HttpMethod.PATCH, "/*/orders").hasAnyRole("USER", "ADMIN")
                                .antMatchers(HttpMethod.GET, "/*/orders/**").hasAnyRole("USER", "ADMIN")
                                .antMatchers(HttpMethod.DELETE, "/*/orders").hasRole("USER")
                                .anyRequest().permitAll()
                )
                .oauth2Login(Customizer.withDefaults());  // (1)


        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

            configuration.setAllowedOrigins(Arrays.asList(
                    "http://localhost:3000",    // 로컬 개발 환경의 프론트엔드 서버 주소 허용
                    "http://bbangorder.s3-website.ap-northeast-2.amazonaws.com" // S3에 호스팅된 프론트엔드 웹사이트 서버 주소 허용
            ));

            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE"));
            configuration.setAllowedHeaders(Arrays.asList("*"));
            configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
