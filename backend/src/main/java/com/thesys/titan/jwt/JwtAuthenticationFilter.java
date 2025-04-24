package com.thesys.titan.jwt;

// import com.coupang.recruitingportalbackend.mem.dto.MemInflowDto;
// import com.coupang.recruitingportalbackend.mem.dto.CustomUserDetails;
// import com.coupang.recruitingportalbackend.mem.dto.MemberDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {

        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        // Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            log.debug("token null");
            filterChain.doFilter(request, response);

            // 조건이 해당되면 메소드 종료 (필수)
            return;
        }

        log.debug("authorization now");
        // Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        // 토큰 소멸 시간 검증
        // if (jwtTokenProvider.isExpired(token)) {

        // log.debug("token expired");
        // filterChain.doFilter(request, response);

        // // 조건이 해당되면 메소드 종료 (필수)
        // return;
        // }

        // 토큰에서 username과 role 획득
        // String username = jwtTokenProvider.getUsername(token);
        // String role = jwtTokenProvider.getRole(token);

        // MemInflowDto memInflowDto = new MemInflowDto();
        // memInflowDto.setInfwId(username);
        // memInflowDto.setPw("tempPassword");

        // CustomUserDetails customUserDetails = new CustomUserDetails(memInflowDto);

        // 토큰에서 추출한 사용자 정보를 담은 Authentication 객체 생성
        // Authentication authentication = new
        // UsernamePasswordAuthenticationToken(customUserDetails, null,
        // customUserDetails.getAuthorities());
        // // 세션에 사용자 등록
        // SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}