package ru.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.library.models.Book;
import ru.library.models.Person;
import ru.library.repositories.BooksRepository;
import ru.library.repositories.PeopleRepository;

import java.util.*;
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

    private static boolean isOverdue(Date currentDate, Date dateOfTakingBook) {
        return ((currentDate.getTime() - dateOfTakingBook.getTime()) / (24 * 60 * 60 * 1000)) >= 10;
    }

    public List<Book> getAllBooksTakenByPerson(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()) {
            List<Book> books = booksRepository.findAllByOwner(person.get());
            for (Book book : books) {
                if (book.getCreatedAt() == null) {
                    book.setOverdue(false);
                } else {
                    book.setOverdue(isOverdue(new Date(), book.getCreatedAt()));
                }
            }
            return books;
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

    public Book getBookById(int id) {
        Optional<Book> book = booksRepository.findById(id);
        return book.orElse(null);
    }

    @Transactional
    public void addBook(Book newBook) {
        booksRepository.save(newBook);
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
        Optional<Book> book = booksRepository.findById(bookId);
        if (book.isPresent()) {
            book.get().setOwner(newBorrower);
            book.get().setCreatedAt(new Date());
            newBorrower.getBooks().add(book.get());
        }
    }

    @Transactional
    public void deleteBook(int bookId) {
        booksRepository.deleteById(bookId);
    }
}
