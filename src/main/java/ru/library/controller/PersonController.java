package ru.library.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.library.models.Person;
//import ru.library.dao.BookDAO;
//import ru.library.dao.PersonDAO;
import ru.library.services.PersonValidator;
import ru.library.services.BooksService;
import ru.library.services.PeopleService;

@Controller
@RequestMapping("/people")
public class PersonController {
	private final PeopleService peopleService;
	private final BooksService booksService;
	private final PersonValidator personValidator;

	@Autowired
	public PersonController(PeopleService peopleService, BooksService booksService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.booksService = booksService;
		this.personValidator = personValidator;
	}

	@GetMapping
	public String getAllPeople(Model model) {
		model.addAttribute("people", peopleService.getAllPeople());
		return "people/index_people";
	}

	@GetMapping("/new")
	public String getPageWithAddingPerson(@ModelAttribute("person") Person person) {
		return "people/new_person";
	}

	@GetMapping("/{id}")
	public String getPersonById(Model model, @PathVariable("id") int id) {
		model.addAttribute("personBooks", booksService.getAllBooksTakenByPerson(id));
		model.addAttribute("person", peopleService.getPersonByPersonId(id));
		return "people/show_person";
	}

	@GetMapping("/{id}/edit")
	public String getPageWithEditingPerson(Model model, @PathVariable("id") int id) {
		model.addAttribute("person", peopleService.getPersonByPersonId(id));
		return "people/edit_person";
	}


	@PostMapping()
	public String addPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
		personValidator.validate(person, bindingResult);
		if (bindingResult.hasErrors()) {
			return "people/new_person";
		}

		peopleService.addPerson(person);
		return "redirect:/people";
	}


	@PatchMapping("/{id}")
	public String changePerson(@ModelAttribute("person") @Valid Person person, Model model, BindingResult bindingResult, @PathVariable("id") int id) {
		person.setPersonId(id);

		personValidator.validate(person, bindingResult);

		if (bindingResult.hasErrors()) {
			return "people/edit_person";
		}

		peopleService.changePerson(person, id);
		return "redirect:/people";
	}


	@DeleteMapping("/{id}")
	public String deletePerson(@PathVariable("id") int id) {
		peopleService.deletePerson(id);
		return "redirect:/people";
	}
}
