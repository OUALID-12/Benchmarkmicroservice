package com.benchmark.jaxrs;

import com.benchmark.jaxrs.config.JpaConfig;
import com.benchmark.jaxrs.resource.CategoryItemResource;
import com.benchmark.jaxrs.resource.CategoryResource;
import com.benchmark.jaxrs.resource.ItemResource;
import com.benchmark.jaxrs.service.CategoryService;
import com.benchmark.jaxrs.service.ItemService;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.benchmark.jaxrs.web.NoCacheFilter;

import java.io.IOException;
import java.net.BindException;
import java.net.URI;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private static String firstNonBlank(String... vals) {
        if (vals == null) return null;
        for (String v : vals) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }

    private static int parseIntOrDefault(String val, int def) {
        try { return val == null ? def : Integer.parseInt(val); } catch (Exception e) { return def; }
    }

    public static void main(String[] args) throws IOException {
        logger.info("Starting JAX-RS Jersey application...");

        // Initialize Spring context
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JpaConfig.class);

        // Configure Jersey with resources and JSON
        final ResourceConfig resourceConfig = new ResourceConfig()
            .register(JacksonFeature.class)
            .register(NoCacheFilter.class)
            .register(CategoryResource.class)
            .register(ItemResource.class)
            .register(CategoryItemResource.class)
            // Bridge: when HK2 resolves a resource, supply the Spring-managed instance
            .register(new AbstractBinder() {
                @Override
                protected void configure() {
                    bindFactory(new org.glassfish.hk2.api.Factory<CategoryResource>() {
                        @Override public CategoryResource provide() { return context.getBean(CategoryResource.class); }
                        @Override public void dispose(CategoryResource instance) {}
                    }).to(CategoryResource.class);

                    bindFactory(new org.glassfish.hk2.api.Factory<ItemResource>() {
                        @Override public ItemResource provide() { return context.getBean(ItemResource.class); }
                        @Override public void dispose(ItemResource instance) {}
                    }).to(ItemResource.class);

                    bindFactory(new org.glassfish.hk2.api.Factory<CategoryItemResource>() {
                        @Override public CategoryItemResource provide() { return context.getBean(CategoryItemResource.class); }
                        @Override public void dispose(CategoryItemResource instance) {}
                    }).to(CategoryItemResource.class);
                }
            });

        // Toggle N+1 join fetch mode
        boolean useJoinFetch = Boolean.parseBoolean(firstNonBlank(System.getProperty("USE_JOIN_FETCH"), System.getenv("USE_JOIN_FETCH"), "false"));
        try {
            context.getBean(ItemService.class).setUseJoinFetch(useJoinFetch);
            logger.info("USE_JOIN_FETCH set to {}", useJoinFetch);
        } catch (Exception ignore) {}

        // Server bind settings (configurable)
        String host = firstNonBlank(System.getProperty("HOST"), System.getenv("HOST"), "0.0.0.0");
        int port = parseIntOrDefault(firstNonBlank(System.getProperty("PORT"), System.getProperty("SERVER_PORT"), System.getenv("PORT"), System.getenv("SERVER_PORT")), 8080);
        String basePath = firstNonBlank(System.getProperty("BASE_PATH"), System.getenv("BASE_PATH"), "api").replaceAll("^/+|/+$", "");

        String baseUriStr = String.format("http://%s:%d/%s/", host, port, basePath);
        HttpServer server = null;
        try {
            server = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUriStr), resourceConfig);
        } catch (Exception e) {
            if (e.getCause() instanceof BindException || e instanceof BindException) {
                int fallback = (port == 8080 ? 8081 : port + 1);
                String fallbackUri = String.format("http://%s:%d/%s/", host, fallback, basePath);
                logger.warn("Port {} is busy. Falling back to {}", port, fallback);
                server = GrizzlyHttpServerFactory.createHttpServer(URI.create(fallbackUri), resourceConfig);
                baseUriStr = fallbackUri;
            } else {
                throw e;
            }
        }

        logger.info("Application started at {}", baseUriStr);
        logger.info("Press Ctrl+C to stop...");

        String effectiveUrl = baseUriStr.replace("0.0.0.0", "localhost");
        logger.info("Try: {}categories?page=0&size=5", effectiveUrl);

        // Add shutdown hook
        final HttpServer srv = server;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down application...");
            srv.shutdown();
            context.close();
        }));

        // Keep the application running
        try {
            Thread.currentThread().join();
        } catch (InterruptedException ex) {
            logger.error("Application interrupted", ex);
        }
    }
}
