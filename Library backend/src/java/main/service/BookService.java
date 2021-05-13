package main.service;

import main.entity.Book;

import java.util.List;

public interface BookService {

    List<Book> listBooks();

    List<String> listAvailableBookNames();

    Book findBookById(long id);

    Book findBookByName(String name);

    Book addBook(Book book);

    void deleteBook(long id);
}
