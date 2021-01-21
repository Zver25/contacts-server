package developer.contacts.repositories;


import developer.contacts.domains.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Page<Person> findByEmailContainingOrFirstNameContainingOrLastNameContaining(String email, String firstName, String lastName, Pageable pageable);

    @Query(value = "SELECT * FROM people p WHERE p.email like :query or p.firstName like :query or p.lastName like :query",
            countQuery = "SELECT count(*) FROM people p WHERE p.email like :query or p.firstName like :query or p.lastName like :query",
            nativeQuery = true)
    Page<Person> findWithFilter(String query, Pageable pageable);

}
