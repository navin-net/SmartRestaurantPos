package com.example.smartrestaurant.security.jwt;

import com.example.smartrestaurant.security.UserDetailsImpl;
import com.example.smartrestaurant.security.UserDetailsServiceImpl;
import com.example.smartrestaurant.security.services.TokenRevocationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private final TokenRevocationService tokenRevocationService;

    public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService,
                           TokenRevocationService tokenRevocationService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.tokenRevocationService = tokenRevocationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = header.substring(7);
            Optional<Claims> parsed = jwtUtils.parseClaims(token);
            if (parsed.isPresent() && !tokenRevocationService.isRevoked(token)) {
                Claims claims = parsed.get();
                try {
                    UserDetails user = userDetailsService.loadUserByUsername(claims.getSubject());
                    if (isUsable(user, claims)) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (UsernameNotFoundException | JwtException ignored) {
                    SecurityContextHolder.clearContext();
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    /** User must be enabled and the token must carry the user's current tokenVersion. */
    private boolean isUsable(UserDetails user, Claims claims) {
        if (!user.isEnabled() || !(user instanceof UserDetailsImpl details)) {
            return false;
        }
        Integer tokenVersion = claims.get(JwtUtils.TOKEN_VERSION_CLAIM, Integer.class);
        return tokenVersion != null && tokenVersion == details.getTokenVersion();
    }
}
