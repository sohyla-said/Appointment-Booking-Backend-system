package com.example.SABS.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@Component
////will create a constructor using any final attributes we declare
////@RequiredArgsConstructor
//public class JwtAuthentiactionFilter extends OncePerRequestFilter {
//
//    private final JwtService jwtService;
//    private final UserDetailsService userDetailsService;
//
//    @Autowired
//    public JwtAuthentiactionFilter(JwtService jwtService, UserDetailsService userDetailsService){
//        this.jwtService = jwtService;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    protected void doFilterInternal(@NonNull HttpServletRequest request,
//                                    @NonNull HttpServletResponse response,
//                                    @NonNull FilterChain filterChain)
//            throws ServletException, IOException {
//        //when we make a call we need to pass the JWT auth token with the header
//        //then try to extract the token from the authorization header
//        //first extract the authorization header
//        final String authHeader = request.getHeader("Authorization");
//
//        //if thr authHeader is null, or it doesn't start with "Bearer "
//        // as it bearer tokens start with "Bearer ", then pass the request
//        //and response to the next filter in the chain and return;
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        try {
//            //extract the token from the authorization header
//            //7 bec of "Bearer " is 7 length long
//            final String jwt = authHeader.substring(7);
//            final String email = jwtService.extractUsername(jwt);
//
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//
//            //check if the userEmail is not null and check if the user isn't authenticated yet
//            //as if the user is authenticated we don't need to perform again all the checks
//            // when authentication is null means user is not yet authenticated
//            if (email != null && authentication == null) {
//                //fetch user details from th DB
//                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
//                //check if the token is still valid or not
//                //if the token is valid we need to update the security context and
//                //send request to our dispatcher servlet
//                if (jwtService.isTokenValid(jwt, userDetails)) {
//                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
//                            userDetails,
//                            null,
//                            userDetails.getAuthorities()
//                    );
//                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
////                    securityContext.setAuthentication(token);
////                    SecurityContextHolder.setContext(securityContext);
//                    SecurityContextHolder.getContext().setAuthentication(token);
//
//                }
//            }
//            filterChain.doFilter(request, response);
//        }catch (Exception exception) {
//            logger.error("JWT Authentication failed: {}");
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid or expired JWT token");
//            return;
//        }
//
//    }
//
//}

@Component
public class JwtAuthentiactionFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthentiactionFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ No Authorization header found or invalid format.");
            filterChain.doFilter(request, response);
            return;
        }

        try {

            final String jwt = authHeader.substring(7);
            final String email = jwtService.extractUsername(jwt);
            System.out.println("🔹 Extracted JWT: " + jwt);
            System.out.println("🔹 Extracted Email from JWT: " + email);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
                System.out.println("🔍 User Roles: " + userDetails.getAuthorities());
                System.out.println("✅ User details loaded: " + userDetails.getUsername());

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(token);
                    System.out.println("✅ Authentication set in SecurityContextHolder.");
                } else {
                    System.out.println("❌ JWT token is not valid.");
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            System.out.println("❌ JWT Authentication failed: " + exception.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid or expired JWT token");
        }
    }
}
