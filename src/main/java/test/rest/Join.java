package test.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import test.redis.RedisManager;

import java.util.Map;

@Slf4j
@Path("/join")
@ApplicationScoped
public class Join {
    @Inject
    RedisManager redisManager;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response join(User user) {
        var isOk = addNewUser(user.name);

        if (!isOk) {
            return Response.status(400).entity(Map.of("message", "error")).build();
        }

        var result = redisManager.getJedis().rpush("queue", user.name);

        log.info("result: {}", result);

        if (result < 2) {
            return Response.ok(Map.of("message", "ok")).build();
        }

        var players = redisManager.getJedis().lpop("queue", 2);

        var player1 = players.get(0);
        var player2 = players.get(1);

        log.info("player1: {}", player1);
        log.info("player2: {}", player2);

        // create match

        var matchId = String.valueOf(Math.random() * 1000000);

        redisManager.getJedis().sadd("match:" + matchId, player1, player2);



        return Response.ok(Map.of("message", "ok")).build();
    }

    private boolean addNewUser(String name) {
        long numbers = redisManager.getJedis().sadd("active_names", name);

        return numbers != 0;
    }
}
