package ru.library.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.library.models.Book;
import ru.library.models.Person;
import ru.library.services.BooksService;
import ru.library.services.PeopleService;

@Controller
@RequestMapping("/books")
public class BookController {
	private final BooksService booksService;
	private final PeopleService peopleService;

	@Autowired
	public BookController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }


	@GetMapping
	public String getAllBooks(Model model) {
		model.addAttribute("books", booksService.getAllBooks());
		return "books/index_book";
	}

	@GetMapping("/new")
	public String getPageWithEditingBook(@ModelAttribute("book") Book book) {
		return "books/new_book";
	}

	@GetMapping("/{id}")
	public String getBookById(Model model, @PathVariable("id") int id) {
		model.addAttribute("book", booksService.getBookById(id));
		model.addAttribute("borrower", peopleService.getPersonByBookId(id));
		model.addAttribute("people", peopleService.getAllPeople());
		model.addAttribute("person", new Person());

		return "books/show_book";
	}

	@GetMapping("/{id}/edit")
	public String getBookEditPage(Model model, @PathVariable("id") int bookId) {
		model.addAttribute("book", booksService.getBookById(bookId));

		return "books/edit_book";
	}


	@PostMapping()
	public String createBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "books/new_book";
		}

		booksService.addBook(book);

		return "redirect:/books";
	}


	@PatchMapping("/{id}")
	public String editBookByBookId(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int bookId) {
		if (bindingResult.hasErrors()) {
			return "books/edit_book";
		}

		Person owner = peopleService.getPersonByBookId(bookId);
		book.setOwner(owner);
		booksService.editBookByBookId(book, bookId);

		return "redirect:/books/" + bookId;
	}


	@PatchMapping("/{id}/assign")
	public String assignNewBorrower(@ModelAttribute("person") Person person, @PathVariable("id") int bookId) {
		Person newBorrower = peopleService.getPersonByPersonId(person.getPersonId());
		booksService.changeBookBorrower(newBorrower, bookId);

		return "redirect:/books/" + bookId;
	}

	@PatchMapping("/{id}/release")
	public String releaseBook(@PathVariable("id") int bookId) {
		booksService.releaseBook(bookId);

		return "redirect:/books/" + bookId;
	}


	@DeleteMapping("/{id}")
	public String deleteBook(@PathVariable("id") int bookId) {
		booksService.deleteBook(bookId);

		return "redirect:/books";
	}
}
