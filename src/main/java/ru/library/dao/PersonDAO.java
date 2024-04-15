package ru.library.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.library.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public PersonDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Person> getAllPeople() {
		return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
	}

	public Person getPersonByPersonId(int id) {
		return jdbcTemplate.query("SELECT * FROM person WHERE person_id=?", new Object[]{id},
				new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
	}

	public Person getPersonByBookId(int bookId) {
		return jdbcTemplate.query(
				"SELECT person.name FROM book " +
						"JOIN person ON book.person_id = person.person_id " +
						"WHERE book.book_id=?",
				new Object[]{bookId},
				new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
	}

	public void addPerson(Person person) {
		jdbcTemplate.update("INSERT INTO person(name, year_of_birthday) VALUES (?,?)",
				person.getName(), person.getYearOfBirthday());
	}

	public void changePerson(Person updatedPerson, int id) {
		jdbcTemplate.update("UPDATE person SET name=?, year_of_birthday=? WHERE person_id=?",
				updatedPerson.getName(), updatedPerson.getYearOfBirthday(), id);
	}

	public void deletePerson(int id) {
		jdbcTemplate.update("DELETE FROM person WHERE person_id=?", id);
	}

	public Optional<Person> findPersonByFullName(String fullName) {
		return jdbcTemplate.query("SELECT * FROM person WHERE name=?", new Object[]{fullName},
				new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
	}
}
