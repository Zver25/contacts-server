package developer.contacts.controllers;

import developer.contacts.services.BlockingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class BlockingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockingController.class);
    private BlockingService blockingService;

    @Autowired
    public void setBlockingService(BlockingService blockingService) {
        this.blockingService = blockingService;
    }

    @MessageMapping("/people/block")
    public void block(Long personId, Principal principal) {
        LOGGER.info("Block person #{}, by client '{}'", personId, principal.getName());
        blockingService.blockPerson(personId, principal.getName());
    }

    @MessageMapping("/people/unblock")
    public void unblock(Long personId, Principal principal) {
        LOGGER.info("Unblock person #{}, by client '{}'", personId, principal.getName());
        blockingService.unblockPerson(personId, principal.getName());
    }

    // @todo: send initial blocking list
}
