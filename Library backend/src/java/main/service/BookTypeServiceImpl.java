package main.service;

import main.entity.BookType;
import main.exeption.BookTypeNotFoundExeption;
import main.repository.BookTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookTypeServiceImpl implements BookTypeService {

    @Autowired
    private BookTypeRepository bookTypeRepository;

    @Override
    public List<BookType> listBookTypes() {
        return bookTypeRepository.findAll();
    }

    @Override
    public List<String> listBookTypeNames() {
        return bookTypeRepository.findAll().stream().map(BookType::getName).collect(Collectors.toList());
    }


    @Override
    public BookType findBookTypeById(long id) {
        Optional<BookType> optionalBookType = bookTypeRepository.findById(id);
        if (optionalBookType.isPresent()) {
            return optionalBookType.get();
        }
        else {
            throw new BookTypeNotFoundExeption("Book type not found");
        }
    }

    @Override
    public BookType findBookTypeByName(String name) {
        Optional<BookType> optionalBookType = bookTypeRepository.findByName(name);
        if (optionalBookType.isPresent()) {
            return optionalBookType.get();
        }
        else {
            throw new BookTypeNotFoundExeption("Book type not found");
        }
    }

    @Override
    public BookType addBookType(BookType bookType) {
        return bookTypeRepository.save(bookType);
    }

    @Override
    public void deleteBookType(long id) {
        Optional<BookType> optionalBookType = bookTypeRepository.findById(id);
        if (optionalBookType.isPresent()) {
            bookTypeRepository.delete(optionalBookType.get());
        }
        else {
            throw new BookTypeNotFoundExeption("BookType not found");
        }
    }
}
