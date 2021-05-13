package main.service;

import main.entity.Book;
import main.exeption.BookNotFoundExeption;
import main.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> listBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<String> listAvailableBookNames() {
        return bookRepository.findAll().stream().filter(book -> book.getCnt() != 0).map(Book::getName).collect(Collectors.toList());
    }

    @Override
    public Book findBookById(long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            return optionalBook.get();
        }
        else {
            throw new BookNotFoundExeption("Book not found");
        }
    }

    @Override
    public Book findBookByName(String name) {
        Optional<Book> optionalBook = bookRepository.findByName(name);
        if (optionalBook.isPresent()) {
            return optionalBook.get();
        }
        else {
            throw new BookNotFoundExeption("Book not found");
        }
    }

    @Override
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            bookRepository.delete(optionalBook.get());
        }
        else {
            throw new BookNotFoundExeption("Book not found");
        }
    }
}
