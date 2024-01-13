package com.fvthree.blogpost.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {
	
	private static final Logger logger = LogManager.getLogger(FlywayConfig.class);

    @Value("${flyway.clean.disable}")
    private boolean disableClean;

    @Bean
    public FlywayMigrationStrategy cleanDisabledMigrationStrategy() {
    	
    	logger.info(disableClean);
    	
        return flyway -> {
            if (!disableClean) {
                flyway.clean();
            }
            flyway.migrate();
        };
    }
}
