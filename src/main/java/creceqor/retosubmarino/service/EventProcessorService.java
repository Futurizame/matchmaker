package creceqor.retosubmarino.service;

import creceqor.retosubmarino.model.Event;
import creceqor.retosubmarino.model.User;
import creceqor.retosubmarino.redis.RedisManager;
import io.quarkus.websockets.next.OpenConnections;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@ApplicationScoped
public class EventProcessorService {
    @Inject
    MatchmakingService matchmakingService;

    @Inject
    RedisManager redisManager;

    @Inject
    OpenConnections openConnections;

    public Event processEvent(Event event) {
        switch (event.getType()) {
            case "JOIN":
                return processJoinEvent(event);
            default:
                throw new IllegalArgumentException("Invalid event type: " + event.getType());
        }
    }

    private Event processJoinEvent(Event event) {
        Map<String, Object> eventData = event.getData();
        log.info("Current user joining: {}", eventData.get("user"));
        redisManager.setUserWebsocketConnectionId(
                (String) eventData.get("user"), (String) eventData.get("websocketConnectionId"));
        String matchId = matchmakingService.join(new User((String) eventData.get("user")));
        Event returnedEvent = new Event();
        if (Objects.nonNull(matchId)) {
            returnedEvent.setType("MATCH_READY");
            returnedEvent.setData(Map.of("matchId", matchId));
            Set<String> users = redisManager.getUsersFromMatch(matchId);
            users.stream().filter(user -> !Objects.equals(user, eventData.get("user")))
                    .forEach(user -> {
                        log.info("Other user to notify: {}", user);
                        String connectionId = redisManager.getUserWebsocketConnectionId(user);
                        openConnections.findByConnectionId(connectionId)
                                .ifPresent(connection -> connection.sendTextAndAwait(returnedEvent));
                    });
        } else {
            returnedEvent.setType("VOID");
        }
        return returnedEvent;
    }
}
