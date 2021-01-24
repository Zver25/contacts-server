package developer.contacts.handlers;

import developer.contacts.domains.AnonymousPrincipal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest) {
            return new AnonymousPrincipal(((ServletServerHttpRequest) request).getServletRequest().getSession().getId());
        }
        return new AnonymousPrincipal(UUID.randomUUID().toString());
    }
}
