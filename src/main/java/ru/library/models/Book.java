package ru.library.models;

import javax.validation.constraints.*;

public class Book {
	private int bookId;
	private int personId;

	@NotEmpty(message = "Title should not be empty")
	@Size(min = 1, max = 100, message = "Title of the book should be between 1 and 100 characters")
	private String name;

	@NotEmpty(message = "Author's name should not be empty")
	@Size(min = 1, max = 100, message = "Author's name should be between 1 and 100 characters")
	private String author;

	@Min(value = 1, message = "The year of writing the book should be 1 at least")
	private int year;

	public Book() {
	}

	public Book(int bookId, int personId, String name, String author, int year) {
		this.bookId = bookId;
		this.personId = personId;
		this.name = name;
		this.author = author;
		this.year = year;
	}

	public Book(String name, String author, int year) {
		this.name = name;
		this.author = author;
		this.year = year;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "Book{" +
				"book_id=" + bookId +
				", person_id=" + personId +
				", name='" + name + '\'' +
				", author='" + author + '\'' +
				", year=" + year +
				'}';
	}
}
