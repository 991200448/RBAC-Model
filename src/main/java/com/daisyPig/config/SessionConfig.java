package com.daisyPig.config;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class SessionConfig {

    public SessionConfig(ServerProperties serverProperties) {
        serverProperties.getServlet().getSession().setTimeout(Duration.ofHours(2));
    }
}