package creceqor.retosubmarino.redis;

import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import redis.clients.jedis.UnifiedJedis;

@ApplicationScoped
public class RedisManager {
    private UnifiedJedis jedis;

    @ConfigProperty(name = "app.redis.host", defaultValue = "localhost")
    private String redisHost;

    @ConfigProperty(name = "app.redis.port", defaultValue = "6379")
    private String redisPort;

    @ConfigProperty(name = "app.redis.username", defaultValue = "user")
    private String redisUsername;

    @ConfigProperty(name = "app.redis.password", defaultValue = "password")
    private String redisPassword;

    public void onStart(@Observes StartupEvent ev) {
        jedis = new UnifiedJedis(
                String.format("redis://%s:%s@%s:%s", redisUsername, redisPassword, redisHost, redisPort));
    }

    public boolean addNewUser(String name) {
        long elementsAdded = jedis.sadd("active_names", name);
        return elementsAdded != 0;
    }

    public long pushToQueue(String name) {
        return jedis.rpush("queue", name);
    }

    public List<String> popFromQueue(int count) {
        return jedis.lpop("queue", count);
    }

    public void createMatch(String matchId, String player1, String player2) {
        jedis.sadd("match:" + matchId, player1, player2);
    }
}
