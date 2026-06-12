package com.powerstrike.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/** Habilita @PreAuthorize en los controllers (RBAC por rol — DEF-EXP-04). */
@Configuration
@EnableMethodSecurity
public class MethodSecurityConfig {
}
