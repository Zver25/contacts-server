package developer.contacts.services;

import java.util.Map;

public interface BlockingService {
    Map<Long, String> getBlockingList();

    void blockPerson(Long personId, String client);

    void unblockPerson(Long personId, String client);

    boolean isPersonBlocked(Long personId, String client);

    void sendBlockingList(String client);
}
