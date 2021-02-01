package developer.contacts.controllers;

import developer.contacts.domains.Person;
import developer.contacts.payloads.PersonListResponse;
import developer.contacts.services.BlockingService;
import developer.contacts.services.NotificationService;
import developer.contacts.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/api/people")
public class PersonController {

    private PersonService personService;
    private BlockingService blockingService;
    private NotificationService notificationService;

    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Autowired
    public void setBlockingService(BlockingService blockingService) {
        this.blockingService = blockingService;
    }

    @GetMapping
    public ResponseEntity<PersonListResponse> getList(
            @RequestParam int page,
            @RequestParam int perPage,
            @RequestParam String query
    ) {
        Page<Person> personPage;
        if (query.isEmpty()) {
            personPage = personService.findAll(PageRequest.of(page - 1, perPage));
        } else {
            personPage = personService.findWithFilter(query, PageRequest.of(page - 1, perPage));
        }
        return new ResponseEntity<>(new PersonListResponse(personPage.getTotalElements(), personPage.toList()), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public Person get(@PathVariable Long id) throws Exception {
        return personService.findById(id).orElseThrow(() -> new Exception("Person not found"));
    }

    @PutMapping("{id}")
    public Person update(@PathVariable Long id, @RequestBody Person requestPerson, HttpServletRequest request) throws Exception {
        String client = request.getSession().getId();
        if (blockingService.isPersonBlocked(id, client)) {
            throw new Exception("Person blocked");
        }
        Person updatedPerson = personService.findById(id)
                .map(person -> {
                    person.apply(requestPerson);
                    return personService.save(person);
                })
                .orElseThrow(() -> new Exception("Person not found"));
        notificationService.notifyAllClients("/topic/people", updatedPerson);
        return updatedPerson;
    }

    @PostMapping
    public Person create(@RequestBody Person requestPerson) {
        return personService.save(requestPerson);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id, HttpServletRequest request) throws Exception {
        String client = request.getSession().getId();
        if (blockingService.isPersonBlocked(id, client)) {
            throw new Exception("Person blocked");
        }
        notificationService.notifyAllClients("/topic/people/delete", id);
        personService.deleteById(id);
    }

    @MessageMapping("/api/people/change")
    @SendTo("/topic/people")
    public Person change(final Person requestPerson, Principal principal) throws Exception {
        if (blockingService.isPersonBlocked(requestPerson.getId(), principal.getName())) {
            throw new Exception("Person blocked");
        }
        Person updatedPerson;
        if (requestPerson.getId() != null && requestPerson.getId() > 0) {
            // update
            updatedPerson = personService.findById(requestPerson.getId())
                    .map(person -> {
                        person.apply(requestPerson);
                        person = personService.save(person);
                        return person;
                    })
                    .orElseThrow(() -> new Exception("Person not found"));
        } else {
            // create
            updatedPerson = personService.save(requestPerson);
        }
        return updatedPerson;
    }

}
