package com.tedioinfernal.tedioapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

/**
 * Configuração do WebClient para integrações externas
 */
@Configuration
public class WebClientConfig {

    /**
     * Bean do WebClient.Builder para uso nas integrações
     * Configurado com timeouts e pool de conexões
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        
        // Configuração do HttpClient com timeouts
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(30))
                .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)); // 16MB buffer
    }
}
