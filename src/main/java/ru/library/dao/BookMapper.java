package ru.library.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.library.models.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {
	@Override
	public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
		Book book = new Book();
		book.setBookId(rs.getInt("book_id"));
//		book.setPersonId(rs.getInt("person_id"));
		book.setName(rs.getString("name"));
		book.setAuthor(rs.getString("author"));
		book.setYear(rs.getInt("year"));
		return book;
	}
}
