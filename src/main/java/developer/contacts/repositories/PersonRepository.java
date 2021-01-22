package developer.contacts.repositories;


import developer.contacts.domains.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Page<Person> findByEmailContainingOrFirstNameContainingOrLastNameContaining(String email, String firstName, String lastName, Pageable pageable);

    @Query(value = "SELECT p.* FROM people p WHERE p.email like %:query% or p.first_name like %:query% or p.last_name like %:query%",
            countQuery = "SELECT count(*) FROM people p WHERE p.email like %:query% or p.first_name like %:query% or p.last_name like %:query%",
            nativeQuery = true)
    Page<Person> findWithFilter(@Param("query") String query, Pageable pageable);

}
