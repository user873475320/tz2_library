package ru.library.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.library.models.Book;
import ru.library.dao.BookDAO;
import ru.library.dao.PersonDAO;

@Controller
@RequestMapping("/books")
public class BookController {
	private final BookDAO bookDAO;
	private final PersonDAO personDAO;

	@Autowired
	public BookController(BookDAO bookDAO, PersonDAO personDAO) {
		this.bookDAO = bookDAO;
		this.personDAO = personDAO;
	}


	@GetMapping
	public String getAllBooks(Model model) {
		model.addAttribute("books", bookDAO.getAllBooks());
		return "books/index_book";
	}

	@GetMapping("/new")
	public String getPageWithEditingBook(@ModelAttribute("book") Book book) {
		return "books/new_book";
	}

	@GetMapping("/{id}")
	public String getBookById(Model model, @PathVariable("id") int id) {
		model.addAttribute("book", bookDAO.getBookById(id));
		model.addAttribute("borrower", personDAO.getPersonByBookId(id));
		model.addAttribute("people", personDAO.getAllPeople());
		return "books/show_book";
	}

	@GetMapping("/{id}/edit")
	public String getBookEditPage(Model model, @PathVariable("id") int bookId) {
		model.addAttribute("book", bookDAO.getBookById(bookId));
		return "books/edit_book";
	}


	@PostMapping()
	public String createBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "books/new_book";
		}

		bookDAO.addBook(book);
		return "redirect:/books";
	}


	@PatchMapping("/{id}")
	public String editBookByBookId(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int bookId) {
		if (bindingResult.hasErrors()) {
			return "books/edit_book";
		}

		bookDAO.editBookByBookId(book, bookId);
		return "redirect:/books" + bookId;
	}

	@PatchMapping("/{id}/assign")
	public String assignNewBorrower(@ModelAttribute("book") Book book, @PathVariable("id") int bookId) {
		bookDAO.chageBookBorrower(book, bookId);
		return "redirect:/books/" + bookId;
	}

	@PatchMapping("/{id}/release")
	public String releaseBook(@PathVariable("id") int bookId) {
		bookDAO.releaseBook(bookId);
		return "redirect:/books/" + bookId;
	}

	@DeleteMapping("/{id}")
	public String deleteBook(@PathVariable("id") int bookId) {
		bookDAO.deleteBook(bookId);
		return "redirect:/books";
	}
}
