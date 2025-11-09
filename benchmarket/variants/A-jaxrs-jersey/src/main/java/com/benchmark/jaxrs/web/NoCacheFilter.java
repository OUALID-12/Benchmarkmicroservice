package com.benchmark.jaxrs.web;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class NoCacheFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().putSingle("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        responseContext.getHeaders().putSingle("Pragma", "no-cache");
    }
}

