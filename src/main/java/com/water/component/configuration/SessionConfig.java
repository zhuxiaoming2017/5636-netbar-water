package com.water.component.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Created by pc on 2017/9/22.
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 5 * 24 * 60 * 60)
public class SessionConfig {

}
