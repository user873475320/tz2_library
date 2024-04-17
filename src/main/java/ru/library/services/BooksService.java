package ru.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.library.models.Book;
import ru.library.models.Person;
import ru.library.repositories.BooksRepository;
import ru.library.repositories.PeopleRepository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;
    private final EntityManager entityManager;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository, EntityManager entityManager) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
        this.entityManager = entityManager;
    }

    public List<Book> getAllBooksTakenByPerson(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()) {
            return booksRepository.findAllByOwner(person.get());
        }

        return null;
    }

    public List<Book> getAllBooks() {
        return booksRepository.findAll();
    }

    @Transactional
    public void addBook(Book newBook) {
        booksRepository.save(newBook);
    }

    public Book getBookById(int id) {
        return booksRepository.findById(id).get();
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
