package test;

import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.WebSocket;

@WebSocket(path = "/ws/{user-id}")
public class WebsocketHandler {
    @OnOpen(broadcast = true)
    public String onOpen() {
        return "Hello World";
    }
}
