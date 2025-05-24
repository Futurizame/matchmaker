package creceqor.retosubmarino.service;

import creceqor.retosubmarino.model.Event;
import creceqor.retosubmarino.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ApplicationScoped
public class EventProcessorService {
    @Inject
    MatchmakingService matchmakingService;

    public void processEvent(Event event) {
        switch (event.getType()) {
            case "JOIN":
                matchmakingService.join(new User((String)event.getData().get("user")));
                break;
            default:
                throw new IllegalArgumentException("Invalid event type: " + event.getType());
        }
    }
}
