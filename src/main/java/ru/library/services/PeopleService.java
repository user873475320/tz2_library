package ru.library.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.library.models.Book;
import ru.library.models.Person;
import ru.library.repositories.BooksRepository;
import ru.library.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;

    @Autowired
    public PeopleService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Person> getAllPeople() {
        return peopleRepository.findAll();
    }

    public Person getPersonByPersonId(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            return person.get();
        }
        return null;
    }

    public Person getPersonByBookId(int bookId) {
        Optional<Book> book = booksRepository.findById(bookId);
        if (book.isPresent()) {
            if (book.get().getOwner() != null) {
                Hibernate.initialize(book.get().getOwner().getBooks());
            }
            return book.get().getOwner();
        }
        return null;
    }

    public Optional<Person> findPersonByFullName(String fullName) {
        Optional<Person> person = peopleRepository.findByName(fullName).stream().findAny();
        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            return person;
        }
        return person;
    }

    @Transactional
    public void addPerson(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void changePerson(Person updatedPerson, int id) {
        Person person = peopleRepository.findById(id).orElse(null);

        person.setName(updatedPerson.getName());
        person.setYearOfBirthday(updatedPerson.getYearOfBirthday());
        person.setBooks(updatedPerson.getBooks());
    }

    @Transactional
    public void deletePerson(int id) {
        peopleRepository.deleteById(id);
    }
}
