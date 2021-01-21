package developer.contacts.controllers;

import developer.contacts.domains.Person;
import developer.contacts.payloads.PersonListResponse;
import developer.contacts.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/people")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    private PersonRepository personRepository;

    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Autowired
    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping
    public ResponseEntity<PersonListResponse> getList(
            @RequestParam int page,
            @RequestParam int perPage,
            @RequestParam String query
    ) {
        Page<Person> personPage;
        if (query.isEmpty()) {
            personPage = personRepository.findAll(PageRequest.of(page - 1, perPage));
        } else {
            personPage = personRepository.findWithFilter(query, PageRequest.of(page, perPage));
        }
        return new ResponseEntity<>(new PersonListResponse(personPage.getTotalElements(), personPage.toList()), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public Person get(@PathVariable Long id) throws Exception {
        return personRepository.findById(id).orElseThrow(() -> new Exception("Person not found"));
    }

    @PutMapping("{id}")
    public Person update(@PathVariable Long id, @RequestBody Person requestPerson) throws Exception {
        Person updatedPerson = personRepository.findById(id)
                .map(person -> {
                    person.apply(requestPerson);
                    return personRepository.save(person);
                })
                .orElseThrow(() -> new Exception("Person not found"));
        simpMessagingTemplate.convertAndSend("/topic/people", updatedPerson);
        return updatedPerson;
    }

    @PostMapping
    public Person newPerson(@RequestBody Person requestPerson) {
        return personRepository.save(requestPerson);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        personRepository.deleteById(id);
    }

    @MessageMapping("/api/people/change")
    @SendTo("/topic/people")
    public Person change(final Person requestPerson) throws Exception {
        Person updatedPerson;
        if (requestPerson.getId() != null && requestPerson.getId() > 0) {
            // update
            updatedPerson = personRepository.findById(requestPerson.getId())
                    .map(person -> {
                        person.apply(requestPerson);
                        person = personRepository.save(person);
                        return person;
                    })
                    .orElseThrow(() -> new Exception("Person not found"));
        } else {
            // create
            updatedPerson = personRepository.save(requestPerson);
        }
        return updatedPerson;
    }

}
