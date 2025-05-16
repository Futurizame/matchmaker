package test.redis;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.Getter;
import redis.clients.jedis.UnifiedJedis;

@ApplicationScoped
public class RedisManager {
    @Getter
    UnifiedJedis jedis;

    void onStart(@Observes StartupEvent ev) {
        jedis = new UnifiedJedis("redis://localhost:6379");
    }
}
