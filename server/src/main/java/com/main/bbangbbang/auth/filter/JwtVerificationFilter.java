package com.main.bbangbbang.auth.filter;

import com.main.bbangbbang.auth.controller.AuthController;
import com.main.bbangbbang.auth.jwt.JwtTokenizer;
import com.main.bbangbbang.auth.utils.CustomAuthorityUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class JwtVerificationFilter extends OncePerRequestFilter {  // (1)   JWT 전용 Filter로 적합
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;

    public JwtVerificationFilter(JwtTokenizer jwtTokenizer,
                                 CustomAuthorityUtils authorityUtils) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
    }

    // 실제로 필터링을 수행하는 메서드, JWT를 검증하고 인증 정보를 SecurityContextHolder에 저장. 예외가 발생하면 적절한 응답 코드를 설정
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Map<String, Object> claims = verifyJws(request, response);
            setAuthenticationToContext(claims);
        } catch (ResponseStatusException be) {
            if (be.getStatus() == HttpStatus.UNAUTHORIZED) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, be.getMessage());
                return;
            }
            request.setAttribute("exception", be);
        } catch (SignatureException se) {
            request.setAttribute("exception", se);
        } catch (ExpiredJwtException ee) {
            request.setAttribute("exception", ee);
        } catch (Exception e) { //  최고조상 -> 마지막 catch문에
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }

    // 필터를 적용할지 여부를 결정하는 메서드. Authorization 헤더가 없거나 Bearer로 시작하지 않으면 필터를 적용하지 않음
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String authorization = request.getHeader("Authorization");

        return authorization == null || !authorization.startsWith("Bearer");
    }

    // JWT를 검증하고 해당하는 클레임을 추출하는 메서드. 만료된 토큰 또는 블랙리스트에 있는 토큰인 경우 예외를 던짐
    // 현재 블랙리스트 수정중이라서 getTokenBlackList 사용못함
    private Map<String, Object> verifyJws(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String jws = request.getHeader("Authorization").replace("Bearer ", "");

        if (jwtTokenizer.isTokenBlacklisted(jws)) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Blacklisted JWT Token");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();

        return claims;
    }

    // 추출한 클레임을 기반으로 Spring Security의 Authentication 객체를 만들어 SecurityContextHolder에 저장하는 메서드
    private void setAuthenticationToContext(Map<String, Object> claims) {
        String username = (String) claims.get("username");
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities((List)claims.get("roles"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
