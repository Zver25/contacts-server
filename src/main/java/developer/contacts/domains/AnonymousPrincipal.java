package developer.contacts.domains;

import java.security.Principal;

public class AnonymousPrincipal implements Principal {

    private final String sessionId;

    public AnonymousPrincipal(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String getName() {
        return sessionId;
    }
}
