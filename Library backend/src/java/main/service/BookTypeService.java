package main.service;

import main.entity.BookType;

import java.util.List;
import java.util.Optional;

public interface BookTypeService {
    List<BookType> listBookTypes();

    List<String> listBookTypeNames();

    BookType findBookTypeById(long id);

    BookType findBookTypeByName(String name);

    BookType addBookType(BookType bookType);

    void deleteBookType(long id);
}
