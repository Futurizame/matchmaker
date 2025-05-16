package test.websocket;

import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.PathParam;
import io.quarkus.websockets.next.WebSocket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebSocket(path = "/ws/{userId}")
public class WebsocketHandler {
    @OnOpen(broadcast = true)
    public String onOpen(@PathParam("userId") String userId) {
        log.info("WebSocket connection opened");
        return "Hello " + userId;
    }
}
