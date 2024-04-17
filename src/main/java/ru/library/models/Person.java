package ru.library.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "person")
public class Person {
	@Id
	@Column(name="person_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int personId;

	@NotEmpty(message = "Name should not be empty")
	@Size(min = 3, max = 50, message = "Name length should be between 3 and 50 characters")
	@Pattern(regexp = "[A-Z]\\w+ [A-Z]\\w+", message = "Your name should be in this format: Ivan Ivanov")
	@Column(name="name")
	private String name;

	@Min(value = 1, message = "Age should be greater than 0")
	@Column(name="year_of_birthday")
	private int yearOfBirthday;

	@OneToMany(mappedBy = "owner")
	private List<Book> books;

	public Person(String name, int yearOfBirthday) {
		this.name = name;
		this.yearOfBirthday = yearOfBirthday;
	}

	public Person() {
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int id) {
		this.personId = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getYearOfBirthday() {
		return yearOfBirthday;
	}

	public void setYearOfBirthday(int yearOfBirthday) {
		this.yearOfBirthday = yearOfBirthday;
	}

	@Override
	public String toString() {
		return "Person{" +
				"personId=" + personId +
				", name='" + name + '\'' +
				", yearOfBirthday=" + yearOfBirthday +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Person person = (Person) o;
		return personId == person.personId && yearOfBirthday == person.yearOfBirthday && Objects.equals(name, person.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(personId, name, yearOfBirthday);
	}
}
