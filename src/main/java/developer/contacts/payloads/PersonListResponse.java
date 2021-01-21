package developer.contacts.payloads;

import developer.contacts.domains.Person;

public class PersonListResponse {
    private final long count;
    private final Iterable<Person> list;

    public PersonListResponse(long count, Iterable<Person> list) {
        this.count = count;
        this.list = list;
    }

    public long getCount() {
        return count;
    }

    public Iterable<Person> getList() {
        return list;
    }

}
