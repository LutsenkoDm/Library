package main;

import main.entity.Book;
import main.entity.BookType;
import main.entity.Client;
import main.entity.JournalRecord;
import main.repository.BookRepository;
import main.repository.BookTypeRepository;
import main.repository.ClientRepository;
import main.repository.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;


@SpringBootApplication//(exclude = {SecurityAutoConfiguration.class})
public class KursWorkApplication {

    @Autowired
    private BookTypeRepository bookTypeRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private JournalRepository journalRepository;

    public static void main(String[] args) {
        SpringApplication.run(KursWorkApplication.class, args);
    }

    @Bean
    public void storeDataBase() {

        int numberOfBookTypes = 5;
        int numberOfBooks = 10;
        int numberOfClients = 10;
        int numberOfJournalRecords = 50;

        for (long i = 1; i <= numberOfBookTypes; i++) {
            BookType bookType = new BookType();
            bookType.setName("type" + i);
            bookType.setFine(i);
            bookType.setDayCount(i);
            bookTypeRepository.save(bookType);
        }

        for (long i = 1; i <= numberOfBooks; i++) {
            Book book = new Book();
            book.setName("book" + i);
            book.setCnt(i);
            book.setTypeId((i + numberOfBookTypes) % numberOfBookTypes + 1);
            bookRepository.save(book);
        }

        for (long i = 1; i <= numberOfClients; i++) {
            Client client = new Client();
            client.setFirstName("firstName" + i);
            client.setLastName("lastName" + i);
            client.setPatherName("patherName" + i);
            client.setPassportSeria("passportSeria" + i);
            client.setPassportNum("passportNum" + i);
            clientRepository.save(client);
        }

        for (long i = 1; i <= numberOfJournalRecords; i++) {
            JournalRecord journalRecord = new JournalRecord();
            journalRecord.setBookId((i + numberOfBooks) % numberOfBooks + 1);
            journalRecord.setClientId((i + numberOfClients) % numberOfClients + 1);
            journalRecord.setDateBeg(new Date(i*10000));
            journalRecord.setDateEnd(new Date(i*20000));
            journalRecord.setDateRet(new Date(i*30000));
            journalRepository.save(journalRecord);
        }


    }
}

