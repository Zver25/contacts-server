package developer.contacts.services;

import developer.contacts.payloads.BlockingPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BlockingServiceImpl implements BlockingService {

    private static final String BLOCK_DESTINATION = "/topic/people/block";
    private static final String UNBLOCK_DESTINATION = "/topic/people/unblock";
    private static final String BLOCKING_LIST_DESTINATION = "/topic/people/blockingList";

    private final Map<Long, String> blockingList = new HashMap<>();
    private NotificationService notificationService;

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public Map<Long, String> getBlockingList() {
        return blockingList;
    }

    @Override
    public void blockPerson(Long personId, String client) {
        if (!blockingList.containsKey(personId)) {
            blockingList.put(personId, client);
            BlockingPayload payload = new BlockingPayload(personId, client);
            notificationService.notifyAllClients(BLOCK_DESTINATION, payload);
        }
    }

    @Override
    public void unblockPerson(Long personId, String client) {
        if (blockingList.get(personId).equals(client)) {
            blockingList.remove(personId);
            notificationService.notifyAllClients(UNBLOCK_DESTINATION, personId);
        }
    }

    @Override
    public void sendBlockingList(String client) {
        notificationService.notifyClient(client, BLOCKING_LIST_DESTINATION, blockingList);
    }

    @Override
    public boolean isPersonBlocked(Long personId, String client) {
        if (!blockingList.containsKey(personId)) {
            return false;
        }
        return !blockingList.get(personId).equals(client);
    }
}
