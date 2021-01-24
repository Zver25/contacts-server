package developer.contacts.payloads;

public class BlockingPayload {

    private final Long personId;
    private final String client;

    public BlockingPayload(Long personId, String client) {
        this.personId = personId;
        this.client = client;
    }

    public Long getPersonId() {
        return personId;
    }

    public String getClient() {
        return client;
    }
}
