package developer.contacts.domains;

import javax.persistence.*;

@Table(name = "people")
@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String firstName;
    private String lastName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format("Person: {id = %d, lastName = %s, firstName = %s, email = %s}",
                id, firstName, lastName, email
        );
    }

    public void apply(Person person) {
        this.email = person.email;
        this.firstName = person.firstName;
        this.lastName = person.lastName;
    }
}
