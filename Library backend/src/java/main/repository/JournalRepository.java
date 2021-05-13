package main.repository;

import main.entity.JournalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface JournalRepository extends JpaRepository<JournalRecord, Long> {
}
