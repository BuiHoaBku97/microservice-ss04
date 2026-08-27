package startup.vn.apigateway.Logs;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ResponseHeaderFilter implements GlobalFilter, Ordered {

    private static final String HEADER_NAME = "X-System-Name";
    private static final String HEADER_VALUE = "Api-Gateway-System";

    @Override
    public int getOrder() {
        return -2;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getResponse().beforeCommit(() -> {
            exchange.getResponse().getHeaders().set(HEADER_NAME, HEADER_VALUE);
            return Mono.empty();
        });
        return chain.filter(exchange);
    }
}
