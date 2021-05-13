package main.repository;

import main.entity.BookType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookTypeRepository extends JpaRepository<BookType, Long> {
    Optional<BookType> findByName(String name);
}
