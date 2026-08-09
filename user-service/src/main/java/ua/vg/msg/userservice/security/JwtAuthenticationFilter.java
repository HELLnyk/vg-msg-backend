package ua.vg.msg.userservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.vg.msg.userservice.repository.UserRepository;
import ua.vg.msg.userservice.repository.entity.UserEntity;
import ua.vg.msg.userservice.service.tokenprovider.AccessTokenProvider;

import java.io.IOException;
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
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (accessTokenProvider.isValid(token)) {
                Optional<UserEntity> user = userRepository.findById(accessTokenProvider.extractUserId(token));
                if (user.isPresent()) {
                    UserEntity userEntity = user.get();
                    UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(
                            userEntity.getId(),
                            null,
                            userEntity.getUserType().getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(upToken);
                }

            }
        }
        filterChain.doFilter(request, response);
    }
}
