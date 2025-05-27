package creceqor.retosubmarino.service;

import java.util.List;

import creceqor.retosubmarino.model.User;
import creceqor.retosubmarino.redis.RedisManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class MatchmakingService {
    @Inject
    RedisManager redisManager;

    public String join(User user) {
        boolean isOk = redisManager.addNewUser(user.getName());

        if (!isOk) {
            throw new RuntimeException("Error adding new user: " + user.getName());
        }

        long result = redisManager.pushToQueue(user.getName());

        log.info("result: {}", result);

        if (result < 2) {
            return null;
        }

        List<String> players = redisManager.popFromQueue(2);

        String player1 = players.get(0);
        String player2 = players.get(1);

        log.info("player1: {}", player1);
        log.info("player2: {}", player2);

        // create match
        String matchId = String.valueOf((long) (Math.random() * 1000000));

        redisManager.createMatch(matchId, player1, player2);

        return matchId;
    }
}
