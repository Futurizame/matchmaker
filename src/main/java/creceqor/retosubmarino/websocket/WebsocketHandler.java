package creceqor.retosubmarino.websocket;

import creceqor.retosubmarino.model.Event;
import creceqor.retosubmarino.service.EventProcessorService;
import io.quarkus.websockets.next.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@WebSocket(path = "/ws")
@ApplicationScoped
public class WebsocketHandler {
    @Inject
    EventProcessorService eventProcessorService;

    @OnOpen
    public void onOpen() {
        log.info("WebSocket connection opened");
    }

    @OnError
    public Event onError(Throwable t) {
        Event event = new Event();
        event.setType("ERROR");
        event.setData(Map.of("message", t.getMessage()));
        return event;
    }

    @OnTextMessage
    public Event onTextMessage(Event event, WebSocketConnection webSocketConnection) {
        log.info("Received event type: {}", event.getType());
        if (Objects.isNull(event.getData())) {
            event.setData(new HashMap<>());
        }
        event.getData().put("websocketConnectionId", webSocketConnection.id());
        return eventProcessorService.processEvent(event);
    }
}
