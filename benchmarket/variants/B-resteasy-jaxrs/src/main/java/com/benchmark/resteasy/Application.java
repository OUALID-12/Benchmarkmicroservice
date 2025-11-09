package com.benchmark.resteasy;

import com.benchmark.resteasy.config.JpaConfig;
import com.benchmark.resteasy.resource.CategoryItemResource;
import com.benchmark.resteasy.resource.CategoryResource;
import com.benchmark.resteasy.resource.ItemResource;
import com.benchmark.resteasy.service.CategoryService;
import com.benchmark.resteasy.service.ItemService;
import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.benchmark.resteasy.web.NoCacheFilter;

import java.io.IOException;
import java.net.BindException;

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
        logger.info("Starting RESTEasy JAX-RS application...");

        // Initialize Spring context
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JpaConfig.class);

        // Configure RESTEasy deployment
        ResteasyDeployment deployment = new ResteasyDeployment();
        deployment.getResources().add(context.getBean(CategoryResource.class));
        deployment.getResources().add(context.getBean(ItemResource.class));
        deployment.getResources().add(context.getBean(CategoryItemResource.class));
        deployment.getProviders().add(context.getBean(NoCacheFilter.class));

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
        NettyJaxrsServer server = new NettyJaxrsServer();
        server.setDeployment(deployment);
        server.setHostname(host);
        server.setPort(port);
        server.setRootResourcePath("/" + basePath);

        try {
            server.start();
        } catch (Exception e) {
            if (e.getCause() instanceof BindException || e instanceof BindException) {
                int fallback = (port == 8080 ? 8081 : port + 1);
                String fallbackUri = String.format("http://%s:%d/%s/", host, fallback, basePath);
                logger.warn("Port {} is busy. Falling back to {}", port, fallback);
                server.setPort(fallback);
                server.start();
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
        final NettyJaxrsServer srv = server;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down application...");
            srv.stop();
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
