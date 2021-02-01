package developer.contacts.services;

public interface NotificationService {
    void addClient(String client);

    void removeClient(String client);

    void notifyAllClients(String destination, Object payload);

    void notifyClient(String client, String destination, Object payload);
}
