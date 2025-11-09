
package com.benchmark.springmvc.web;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class NoCacheFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (response instanceof HttpServletResponse r) {
            r.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
            r.setHeader("Pragma", "no-cache");
        }
        chain.doFilter(request, response);
    }
}
