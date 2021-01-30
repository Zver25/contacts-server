package developer.contacts.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class NotificationService {

    private SimpMessagingTemplate simpMessagingTemplate;
    private final Set<String> clients = new HashSet<>();

    @Autowired
    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void addClient(String client) {
        clients.add(client);
    }

    public void removeClient(String client) {
        clients.remove(client);
    }

    public void notifyAllClients(String destination, Object payload) {
        for (String client: clients) {
            notifyClient(client, destination, payload);
        }
    }

    public void notifyClient(String client, String destination, Object payload) {
        simpMessagingTemplate.convertAndSendToUser(client, destination, payload);
    }

}
