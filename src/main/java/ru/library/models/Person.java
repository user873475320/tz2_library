package ru.library.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Person {
	private int personId;

	@NotEmpty(message = "Name should not be empty")
	@Size(min=3, max = 50, message = "Name length should be between 3 and 50 characters")
	@Pattern(regexp = "[A-Z]\\w+ [A-Z]\\w+", message = "Your name should be in this format: Ivan Ivanov")
	private String name;

	@Min(value = 1, message = "Age should be greater than 0")
	private int yearOfBirthday;


	public Person(int personId, String name, int yearOfBirthday) {
		this.personId = personId;
		this.name = name;
		this.yearOfBirthday = yearOfBirthday;
	}

	public Person() {
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
}
