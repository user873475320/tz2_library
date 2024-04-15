package ru.library.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.library.models.Book;

import java.util.List;

@Component
public class BookDAO {
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public BookDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Book> getAllBooksTakenByPerson(int id) {
		return jdbcTemplate.query(
				"SELECT book.name, book.author, book.year FROM person JOIN book ON person.person_id = book.person_id AND person.person_id=?",
				new Object[]{id}, new BeanPropertyRowMapper<>(Book.class));
	}

	public List<Book> getAllBooks() {
		return jdbcTemplate.query("SELECT * FROM book", new BookMapper());
	}

	public void addBook(Book newBook) {
		jdbcTemplate.update("INSERT INTO book(name, author, year) VALUES (?,?,?)",
				newBook.getName(), newBook.getAuthor(), newBook.getYear());
	}

	public Book getBookById(int id) {
		return jdbcTemplate.query("SELECT * FROM book WHERE book_id=?", new Object[]{id},
				new BookMapper()).stream().findAny().orElse(null);
	}

	public void editBookByBookId(Book updatedBook, int bookId) {
		jdbcTemplate.update("UPDATE book SET name=?, author=?, year=? WHERE book_id=?",
				updatedBook.getName(), updatedBook.getAuthor(),
				updatedBook.getYear(), bookId);
	}

	public void releaseBook(int bookId) {
		jdbcTemplate.update("UPDATE book SET person_id = NULL WHERE book_id=?", bookId);
	}

	public void chageBookBorrower(Book updatedBook, int bookId) {
		jdbcTemplate.update("UPDATE book SET person_id=? WHERE book_id=?",
				updatedBook.getPersonId(), bookId);
	}

	public void deleteBook(int bookId) {
		jdbcTemplate.update("DELETE FROM book WHERE book_id=?");
	}
}
