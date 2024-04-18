package ru.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.library.models.Book;
import ru.library.models.Person;
import ru.library.repositories.BooksRepository;
import ru.library.repositories.PeopleRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

//    public Page<Book> findPaginated(Pageable pageable, List<Book> books) {
//        int pageSize = pageable.getPageSize();
//        int currentPage = pageable.getPageNumber();
//        int startItem = currentPage * pageSize;
//        List<Book> list;
//
//        if (books.size() < startItem) {
//            list = Collections.emptyList();
//        } else {
//            int toIndex = Math.min(startItem + pageSize, books.size());
//            list = books.subList(startItem, toIndex);
//        }
//
//        Page<Book> bookPage = new PageImpl<Book>(list, PageRequest.of(currentPage, pageSize), books.size());
//
//        return bookPage;
//    }

    public List<Book> getAllBooksTakenByPerson(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()) {
            return booksRepository.findAllByOwner(person.get());
        }

        return null;
    }

    public Page<Book> getAllBooksSortedByYear(int currentPage, int booksPerPage) {
        return booksRepository.findAll(PageRequest.of(currentPage, booksPerPage, Sort.by("year")));
    }

    public List<Book> getAllBooksSortedByYear() {
        return booksRepository.findAll().stream().sorted((Comparator.comparingInt(Book::getYear))).collect(Collectors.toList());
    }

    public Page<Book> getAllBooks(Pageable pageable) {
        return booksRepository.findAll(pageable);
    }

    public List<Book> getAllBooks() {
        return booksRepository.findAll();
    }

    public List<Book> getBookStartingWith(String prefix) {
        return booksRepository.findBookByNameStartingWith(prefix);
    }

    @Transactional
    public void addBook(Book newBook) {
        booksRepository.save(newBook);
    }

    public Book getBookById(int id) {
        Optional<Book> book = booksRepository.findById(id);
        return book.orElse(null);
    }

    @Transactional
    public void editBookByBookId(Book updatedBook, int bookId) {
        updatedBook.setBookId(bookId);

        booksRepository.save(updatedBook);
    }

    @Transactional
    public void releaseBook(int bookId) {
        Optional<Book> book = booksRepository.findById(bookId);
        book.ifPresent(value -> value.setOwner(null));
    }

    @Transactional
    public void changeBookBorrower(Person newBorrower, int bookId) {
//        jdbcTemplate.update("UPDATE book SET person_id=? WHERE book_id=?",
//                updatedBook.getPersonId(), bookId);
        Optional<Book> book = booksRepository.findById(bookId);
        if (book.isPresent()) {
            book.get().setOwner(newBorrower);
            newBorrower.getBooks().add(book.get());
        }
    }

    @Transactional
    public void deleteBook(int bookId) {
        booksRepository.deleteById(bookId);
    }
}
