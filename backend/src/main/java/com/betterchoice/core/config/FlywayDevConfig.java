package com.betterchoice.core.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * In dev, repair Flyway checksums when migration files change during active development.
 * Never edit applied migrations in production — add V3, V4, etc. instead.
 */
@Configuration
@Profile("dev")
public class FlywayDevConfig {

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            flyway.repair();
            flyway.migrate();
        };
    }
}
