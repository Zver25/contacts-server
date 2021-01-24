package developer.contacts.services;

import developer.contacts.payloads.BlockingPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class BlockingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockingService.class);

    private static final String BLOCK_DESTINATION = "/topic/people/block";
    private static final String UNBLOCK_DESTINATION = "/topic/people/unblock";
    private static final String BLOCKING_LIST_DESTINATION = "/topic/people/blockingList";

    private final Map<Long, String> blockingList = new HashMap<>();
    private NotificationService notificationService;

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public Map<Long, String> getBlockingList() {
        return blockingList;
    }

    public void blockPerson(Long personId, String client) {
        if (!blockingList.containsKey(personId)) {
            blockingList.put(personId, client);
            BlockingPayload payload = new BlockingPayload(personId, client);
            notificationService.notifyAllClients(BLOCK_DESTINATION, payload);
        }
    }

    public void unblockPerson(Long personId, String client) {
        if (blockingList.get(personId).equals(client)) {
            blockingList.remove(personId);
            notificationService.notifyAllClients(UNBLOCK_DESTINATION, personId);
        }
    }

    public void sendBlockingList(String client) {
        notificationService.notifyClient(client, BLOCKING_LIST_DESTINATION, blockingList);
    }

    public boolean isPersonBlocked(Long personId, String client) {
        if (!blockingList.containsKey(personId)) {
            return false;
        }
        return !blockingList.get(personId).equals(client);
    }
}
