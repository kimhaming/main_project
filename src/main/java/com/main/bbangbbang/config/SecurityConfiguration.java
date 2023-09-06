package com.main.bbangbbang.config;

import com.main.bbangbbang.auth.handler.OAuth2LoginSuccessHandler;
import com.main.bbangbbang.jwt.JwtTokenizer;
import com.main.bbangbbang.utils.CustomAuthorityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//  OAuth2 사용자가 로그인 성공 시에 대한 성공 및 실패에 대한 특정 핸들러 작업으로 처리로 변경(버전업)해주며 불필요
//  import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
//  사용자 정보 기본값 -> UserInfo 변경되면서 불필요
//  import static org.springframework.transaction.TransactionDefinition.withDefaults;

//
@Configuration
public class SecurityConfiguration {

    //  JwtTokenizer 객체를 빈으로 등록
    @Bean
    public JwtTokenizer tokenizer(){
        return new JwtTokenizer();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http    //  시큐리티 설정 커스터마이징 수정 필요
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
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(new OAuth2LoginSuccessHandler(
                                tokenizer(),    // tokenizer() 메서드 호출하여 인스턴스 사용
                                new CustomAuthorityUtils()
                        ))
                        .failureHandler((request, response, exception) -> {
                            System.out.println("OAuth2LoginAuthenticationFailureHandler");
                            exception.printStackTrace();
                        }));  // (1)


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

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> userInfoService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return (userRequest) -> {
            OAuth2User user = delegate.loadUser(userRequest);
            System.out.println("OAuth2User: " + user);  //  디버깅 확인

            // user token is valid => jwt token generate

            // token is new -> save to db

            return user;
        };
    }
}
