package io.vertx.example;

import io.vertx.example.dto.Counter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

/**
 * A configuration bean.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@Configuration
public class AppConfiguration {

    @Bean
    @Qualifier("typeCounter")
    public ConcurrentHashMap<String , Counter> typeCounter() {
        return new ConcurrentHashMap<>();
    }
    @Bean
    @Qualifier("dataCounter")
    public ConcurrentHashMap<String , Counter> dataCounter() {
        return new ConcurrentHashMap<>();
    }
}
