package com.benchmark.resteasy.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.benchmark.resteasy")
public class JpaConfig {

    private static final Logger log = LoggerFactory.getLogger(JpaConfig.class);

    private static String firstNonBlank(String... vals) {
        if (vals == null) return null;
        for (String v : vals) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }

    private static String cfg(String key, String def) {
        // Prefer -D system properties, then environment variables, then default
        String fromSysProp = System.getProperty(key);
        String fromEnv = System.getenv(key);
        return firstNonBlank(fromSysProp, fromEnv, def);
    }

    @Bean
    public DataSource dataSource() {
        String url = cfg("DB_URL", "jdbc:postgresql://localhost:5432/benchmark");
        String user = cfg("DB_USERNAME", "benchmark");
        String pass = cfg("DB_PASSWORD", "benchmark123");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(pass);
        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(10);

        // Helpful startup log without exposing password
        int passLen = (pass == null ? -1 : pass.length());
        log.info("Configuring DataSource: url={}, username={}, passLen={}", url, user, passLen);

        return new HikariDataSource(config);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.benchmark.resteasy.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties props = new Properties();
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.setProperty("hibernate.hbm2ddl.auto", "validate");
        props.setProperty("hibernate.show_sql", "false");
        props.setProperty("hibernate.format_sql", "true");
        // Disable 2nd level and query caches for fair benchmarks
        props.setProperty("hibernate.cache.use_second_level_cache", "false");
        props.setProperty("hibernate.cache.use_query_cache", "false");
        em.setJpaProperties(props);

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
}
