package main.web;

import main.entity.Book;
import main.entity.BookType;
import main.entity.Client;
import main.entity.JournalRecord;
import main.exeption.BookNotFoundExeption;
import main.exeption.BookTypeNotFoundExeption;
import main.exeption.ClientNotFoundExeption;
import main.exeption.JournalNotFoundExeption;
import main.service.BookService;
import main.service.BookTypeService;
import main.service.ClientService;
import main.service.JournalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/lib")
public class LibraryController {

    private final JournalService journalService;
    private final ClientService clientService;
    private final BookService bookService;
    private final BookTypeService bookTypeService;

    public LibraryController(JournalService journalService, ClientService clientService, BookService bookService, BookTypeService bookTypeService) {
        this.journalService = journalService;
        this.clientService = clientService;
        this.bookService = bookService;
        this.bookTypeService = bookTypeService;
    }

    @PostMapping(value = "/addJournalRecord", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JournalRecord> addJournalRecord(@RequestBody JournalRecord newJournalRecord) {
        try {
            Book book = bookService.findBookById(newJournalRecord.getBookId());
            if (book == null) {
                throw new BookNotFoundExeption("Book not found");
            }
            if (book.getCnt() == 0) {
                throw new BookNotFoundExeption("Book cnt = 0");
            }
            if (clientService.findClientById(newJournalRecord.getClientId()) == null) {
                throw new ClientNotFoundExeption("Client not found");
            }
            book.setCnt(book.getCnt() - 1);

            return new ResponseEntity<>(journalService.addJournalRecord(newJournalRecord), HttpStatus.OK);
        }
        catch (BookNotFoundExeption | ClientNotFoundExeption exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    @PostMapping(value = "/addClient", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Client> addClient(@RequestBody Client newClient) {
        return new ResponseEntity<>(clientService.addClient(newClient), HttpStatus.OK);
    }

    @PostMapping(value = "/addBook", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Book> addBook(@RequestBody Book newBook) {
        try {
            if (bookTypeService.findBookTypeById(newBook.getTypeId()) == null) {
                throw new BookTypeNotFoundExeption("Book type not found");
            }
            return new ResponseEntity<>(bookService.addBook(newBook), HttpStatus.OK);
        }
        catch (BookTypeNotFoundExeption exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    @PostMapping(value = "/addBookType", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BookType> addBookType(@RequestBody BookType newBookType) {
        return new ResponseEntity<>(bookTypeService.addBookType(newBookType), HttpStatus.OK);
    }

    @GetMapping("/journal")
    public ResponseEntity<List<JournalRecord>> getJournal() {
        return new ResponseEntity<>(journalService.journal(), HttpStatus.OK);
    }

    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getAllClients() {
        return new ResponseEntity<>(clientService.listClients(), HttpStatus.OK);
    }

    @GetMapping("/clientFirstNames")
    public ResponseEntity<List<String>> getAllClientFirstNames() {
        return new ResponseEntity<>(clientService.listClientFirstNames(), HttpStatus.OK);
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        return new ResponseEntity<>(bookService.listBooks(), HttpStatus.OK);
    }

    @GetMapping("/availableBookNames")
    public ResponseEntity<List<String>> getAvailableBookNames() {
        return new ResponseEntity<>(bookService.listAvailableBookNames(), HttpStatus.OK);
    }

    @GetMapping("/bookTypes")
    public ResponseEntity<List<BookType>> getAllBookTypes() {
        return new ResponseEntity<>(bookTypeService.listBookTypes(), HttpStatus.OK);
    }

    @GetMapping("/bookTypeNames")
    public ResponseEntity<List<String>> getAllBookTypeNames() {
        return new ResponseEntity<>(bookTypeService.listBookTypeNames(), HttpStatus.OK);
    }

    @GetMapping("/journal/{id}")
    public ResponseEntity<JournalRecord> getJournalRecord(@PathVariable("id") long id) {
        try {
            return new ResponseEntity<>(journalService.findJournalRecord(id), HttpStatus.OK);
        }
        catch (JournalNotFoundExeption exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Journal record not found");
        }
    }

    @GetMapping("/clientById/{id}")
    public ResponseEntity<Client> getClient(@PathVariable("id") long id) {
        try {
            return new ResponseEntity<>(clientService.findClientById(id), HttpStatus.OK);
        }
        catch (ClientNotFoundExeption exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        }
    }

    @GetMapping("/clientIdByFirstLastNameAndPassportNumber/{firstName}/{lastName}/{passportNum}")
    public ResponseEntity<Long> getClientIdByFirstLastNameAndPassportNumber(@PathVariable("firstName") String firstName,
                                                                             @PathVariable("lastName") String lastName,
                                                                             @PathVariable("passportNum") String passportNum) {
        try {
            return new ResponseEntity<>(clientService.findClientsByFirstLastNameAndPassportNumber(firstName, lastName, passportNum).getId(), HttpStatus.OK);
        }
        catch (ClientNotFoundExeption exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        }
    }

    @GetMapping("/bookById/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") long id) {
        try {
            return new ResponseEntity<>(bookService.findBookById(id), HttpStatus.OK);
        }
        catch (BookNotFoundExeption exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
    }

    @GetMapping("/bookIdByName/{name}")
    public ResponseEntity<Long> getBookIdByName(@PathVariable("name") String name) {
        try {
            return new ResponseEntity<>(bookService.findBookByName(name).getId(), HttpStatus.OK);
        }
        catch (BookNotFoundExeption exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
    }

    @GetMapping("/bookTypeById/{id}")
    public ResponseEntity<BookType> getBookTypeById(@PathVariable("id") long id) {
        try {
            return new ResponseEntity<>(bookTypeService.findBookTypeById(id), HttpStatus.OK);
        }
        catch (BookTypeNotFoundExeption exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book type not found");
        }
    }

    @GetMapping("/bookTypeIdByName/{name}")
    public ResponseEntity<Long> getBookTypeIdByName(@PathVariable("name") String name) {
        try {
            return new ResponseEntity<Long>(bookTypeService.findBookTypeByName(name).getId(), HttpStatus.OK);
        }
        catch (BookTypeNotFoundExeption exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book type not found");
        }
    }

    @DeleteMapping("/journalRecord/{id}")
    public void deleteJournalRecord(@PathVariable("id") long id) {
        try {
            journalService.deleteJournalRecord(id);
        }
        catch (JournalNotFoundExeption exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Journal record not found");
        }
    }

    @DeleteMapping("/client/{id}")
    public void deleteClient(@PathVariable("id") long id) {
        try {
            clientService.deleteClient(id);
        }
        catch (ClientNotFoundExeption exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        }
    }

    @DeleteMapping("/book/{id}")
    public void deleteBook(@PathVariable("id") long id) {
        try {
            bookService.deleteBook(id);
        }
        catch (BookNotFoundExeption exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
    }

    @DeleteMapping("/bookType/{id}")
    public void deleteBookType(@PathVariable("id") long id) {
        try {
            bookTypeService.deleteBookType(id);
        }
        catch (BookTypeNotFoundExeption exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "BookType not found");
        }
    }

    @PutMapping(value = "/journalRecord/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JournalRecord> updateJournalRecord(@PathVariable("id") long id, @RequestBody JournalRecord newJournalRecord) {
        try {
            JournalRecord updatedJournalRecord = journalService.findJournalRecord(id);
            Book book = bookService.findBookById(newJournalRecord.getBookId());
            if (book == null) {
                throw new BookNotFoundExeption("Book not found");
            }
            if (book.getCnt() == 0) {
                throw new BookNotFoundExeption("Book cnt = 0");
            }
            if (clientService.findClientById(newJournalRecord.getClientId()) == null) {
                throw new ClientNotFoundExeption("Client not found");
            }

            updatedJournalRecord.setBookId(newJournalRecord.getBookId());
            updatedJournalRecord.setClientId(newJournalRecord.getClientId());
            updatedJournalRecord.setDateBeg(newJournalRecord.getDateBeg());
            updatedJournalRecord.setDateEnd(newJournalRecord.getDateEnd());
            updatedJournalRecord.setDateRet(newJournalRecord.getDateRet());
            book.setCnt(book.getCnt() - 1);
            
            return ResponseEntity.ok(journalService.addJournalRecord(updatedJournalRecord));
        }
        catch (JournalNotFoundExeption exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Journal record not found");
        }
        catch (BookNotFoundExeption | ClientNotFoundExeption exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    @PutMapping(value = "/client/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Client> updateClient(@PathVariable("id") long id, @RequestBody Client newClient) {
        try {
            Client updatedClient = clientService.findClientById(id);

            updatedClient.setFirstName(newClient.getFirstName());
            updatedClient.setLastName(newClient.getLastName());
            updatedClient.setPatherName(newClient.getPatherName());
            updatedClient.setPassportSeria(newClient.getPassportSeria());
            updatedClient.setPassportNum(newClient.getPassportNum());

            return ResponseEntity.ok(clientService.addClient(updatedClient));
        }
        catch (ClientNotFoundExeption exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        }
    }

    @PutMapping(value = "/book/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Book> updateBook(@PathVariable("id") long id, @RequestBody Book newBook) {
        try {
            Book updatedBook = bookService.findBookById(id);
            if (bookTypeService.findBookTypeById(newBook.getTypeId()) == null) {
                throw new BookTypeNotFoundExeption("Book type not found");
            }
            updatedBook.setName(newBook.getName());
            updatedBook.setCnt(newBook.getCnt());
            updatedBook.setTypeId(newBook.getTypeId());

            return ResponseEntity.ok(bookService.addBook(updatedBook));
        }
        catch (BookTypeNotFoundExeption exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
        catch (BookNotFoundExeption exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
    }

    @PutMapping(value = "/bookType/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BookType> updateBookType(@PathVariable("id") long id, @RequestBody BookType newBookType) {
        try {
            BookType updatedBookType = bookTypeService.findBookTypeById(id);

            updatedBookType.setName(newBookType.getName());
            updatedBookType.setFine(newBookType.getFine());
            updatedBookType.setDayCount(newBookType.getDayCount());

            return ResponseEntity.ok(bookTypeService.addBookType(updatedBookType));
        }
        catch (BookTypeNotFoundExeption exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "BookType not found");
        }
    }

    @GetMapping("/client/{id}/books")
    public ResponseEntity<Set<Book>> getClientBooks(@PathVariable("id") long id) {
        try {
            Set<Book> clientBooks = new HashSet<>();
            for (long bookId : clientService.getClientBookIds(id)) {
                clientBooks.add(bookService.findBookById(bookId));
            }
            return new ResponseEntity<>(clientBooks, HttpStatus.OK);
        }
        catch (ClientNotFoundExeption exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        }
    }
}
