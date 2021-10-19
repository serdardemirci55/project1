package com.cmpe281.project1.authorization;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Serdar Demirci
 */
public class JwtTokenFilter extends GenericFilterBean {

    private JwtTokenProvider mProvider;

    public JwtTokenFilter(JwtTokenProvider provider) {
        this.mProvider = provider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        try {
            String token = mProvider.resolveToken((HttpServletRequest) servletRequest);
            if (token != null && mProvider.validateToken(token)) {
                Authentication auth = mProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (UsernameNotFoundException exception) {
            System.out.println(exception);
        }
    }
}
