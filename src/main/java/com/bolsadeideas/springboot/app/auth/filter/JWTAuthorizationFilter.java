package com.bolsadeideas.springboot.app.auth.filter;

import com.bolsadeideas.springboot.app.auth.service.JWTService;
import com.bolsadeideas.springboot.app.auth.service.JWTServiceImp;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JWTService jwtService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(JWTServiceImp.HEADER_STRING);

        if (!requiereAutentificacion(header)) {
            chain.doFilter(request, response);
            return;
        }


        UsernamePasswordAuthenticationToken authenticationToken = null;

        if (jwtService.validate(header)) {
            authenticationToken = new UsernamePasswordAuthenticationToken(jwtService.getUsername(header),
                    null, jwtService.getRoles(header));
        }
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }
    
    protected boolean requiereAutentificacion(String header) {
        if (header == null || !header.startsWith(JWTServiceImp.TOKEN_PREFIX)) {
            return false;
        }
        return true;
    }
}
