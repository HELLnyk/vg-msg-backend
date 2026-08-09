package ua.vg.msg.userservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.vg.msg.userservice.repository.entity.UserEntity;
import ua.vg.msg.userservice.service.UserService;
import ua.vg.msg.userservice.service.tokenprovider.AccessTokenProvider;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * JwtAuthenticationFilter — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AccessTokenProvider accessTokenProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (accessTokenProvider.isValid(token)) {
                Optional<UserEntity> user = userService.getUserById(accessTokenProvider.extractUserId(token));
                if (user.isPresent()) {
                    UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(
                            user.get(),
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );
                    SecurityContextHolder.getContext().setAuthentication(upToken);
                }

            }
        }
        filterChain.doFilter(request, response);
    }
}
