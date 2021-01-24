package developer.contacts.events;

import developer.contacts.services.BlockingService;
import developer.contacts.services.NotificationService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
public class StompConnectedEvent implements ApplicationListener<SessionConnectedEvent> {
    private final NotificationService notificationService;
    private final BlockingService blockingService;

    public StompConnectedEvent(NotificationService notificationService, BlockingService blockingService) {
        this.notificationService = notificationService;
        this.blockingService = blockingService;
    }

    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        if (event.getUser() != null) {
            String client = event.getUser().getName();
            notificationService.addClient(client);
            blockingService.sendBlockingList(client);
        }
    }
}
