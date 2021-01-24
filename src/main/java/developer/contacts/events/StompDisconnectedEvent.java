package developer.contacts.events;

import developer.contacts.services.NotificationService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class StompDisconnectedEvent implements ApplicationListener<SessionDisconnectEvent> {
    private final NotificationService notificationService;

    public StompDisconnectedEvent(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        if (event.getUser() != null) {
            notificationService.removeClient(event.getUser().getName());
        }
    }
}
