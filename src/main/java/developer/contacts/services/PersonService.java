package developer.contacts.services;

import developer.contacts.domains.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PersonService {

    Page<Person> findAll(Pageable pageable);
    Page<Person> findWithFilter(String query, Pageable pageable);
    Optional<Person> findById(Long id);
    Person save(Person person);
    void deleteById(Long id);
}
