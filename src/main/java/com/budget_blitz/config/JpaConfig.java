package com.budget_blitz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class JpaConfig {
}
