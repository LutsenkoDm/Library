package main.repository;

import main.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<List<Client>> findAllByFirstNameAndLastNameAndPassportNum(String firstName, String lastName, String passportNum);

}
