package creceqor.retosubmarino.websocket;

import creceqor.retosubmarino.model.Event;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebSocket(path = "/ws")
@ApplicationScoped
public class WebsocketHandler {
    @OnOpen
    public void onOpen() {
        log.info("WebSocket connection opened");
    }

    @OnTextMessage
    public String onTextMessage(Event event) {
        log.info("Received event type: {}", event.getType());
        return "Echo: ";
    }
}
