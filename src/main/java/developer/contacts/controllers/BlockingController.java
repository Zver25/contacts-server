package developer.contacts.controllers;

import developer.contacts.payloads.BlockingPayload;
import developer.contacts.services.BlockingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @SubscribeMapping("/people/blockingList")
    public List<BlockingPayload> initialBlock() {
        List<BlockingPayload> payload = new ArrayList<>();
        for (Map.Entry<Long, String> blocking: blockingService.getBlockingList().entrySet()) {
            payload.add(new BlockingPayload(blocking.getKey(), blocking.getValue()));
        }
        return payload;
    }
}
