package startup.vn.orderservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpClientConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(
            @Value("${http.client.connect-timeout-ms:2000}") int connectTimeoutMs,
            @Value("${http.client.read-timeout-ms:3000}") int readTimeoutMs
    ) {
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeoutMs);
        requestFactory.setReadTimeout(readTimeoutMs);
        return new RestTemplate(requestFactory);
    }
}
