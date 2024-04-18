package ru.library.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "book")
public class Book {
	@Id
	@Column(name = "book_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int bookId;

	@NotEmpty(message = "Title should not be empty")
	@Size(min = 1, max = 100, message = "Title of the book should be between 1 and 100 characters")
	@Column(name="name")
	private String name;

	@NotEmpty(message = "Author's name should not be empty")
	@Size(min = 1, max = 100, message = "Author's name should be between 1 and 100 characters")
	@Column(name="author")
	private String author;

	@Min(value = 1, message = "The year of writing the book should be 1 at least")
	@Column(name="year")
	private int year;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="person_id")
	private Person owner;

	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Transient
	private boolean isOverdue;


	public Book() {
	}

	public Book(String name, String author, int year) {
		this.name = name;
		this.author = author;
		this.year = year;
	}


	public boolean isOverdue() {
		return isOverdue;
	}

	public void setOverdue(boolean overdue) {
		isOverdue = overdue;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
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
				", name='" + name + '\'' +
				", author='" + author + '\'' +
				", year=" + year +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Book book = (Book) o;
		return bookId == book.bookId && year == book.year && Objects.equals(name, book.name) && Objects.equals(author, book.author);
	}

	@Override
	public int hashCode() {
		return Objects.hash(bookId, name, author, year);
	}
}
