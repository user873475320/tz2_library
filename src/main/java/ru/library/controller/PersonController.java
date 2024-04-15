package ru.library.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.library.models.Person;
import ru.library.dao.BookDAO;
import ru.library.dao.PersonDAO;
import ru.library.dao.PersonValidator;

@Controller
@RequestMapping("/people")
public class PersonController {
	private final PersonDAO personDAO;
	private final BookDAO bookDAO;
	private final PersonValidator personValidator;

	@Autowired
	public PersonController(PersonDAO personDAO, BookDAO bookDAO, PersonValidator personValidator) {
		this.personDAO = personDAO;
		this.bookDAO = bookDAO;
		this.personValidator = personValidator;
	}

	@GetMapping
	public String getAllPeople(Model model) {
		model.addAttribute("people", personDAO.getAllPeople());
		return "people/index_people";
	}

	@GetMapping("/new")
	public String getPageWithAddingPerson(@ModelAttribute("person") Person person) {
		return "people/new_person";
	}

	@GetMapping("/{id}")
	public String getPersonById(Model model, @PathVariable("id") int id) {
		model.addAttribute("personBooks", bookDAO.getAllBooksTakenByPerson(id));
		model.addAttribute("person", personDAO.getPersonByPersonId(id));
		return "people/show_person";
	}

	@GetMapping("/{id}/edit")
	public String getPageWithEditingPerson(Model model, @PathVariable("id") int id) {
		model.addAttribute("person", personDAO.getPersonByPersonId(id));
		return "people/edit_person";
	}


	@PostMapping()
	public String addCreatedPersonToDB(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
		personValidator.validate(person, bindingResult);
		if (bindingResult.hasErrors()) {
			return "people/new_person";
		}

		personDAO.addPerson(person);
		return "redirect:/people";
	}


	@PatchMapping("/{id}")
	public String changePerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id) {
		personValidator.validate(person, bindingResult);

		if (bindingResult.hasErrors()) {
			return "people/edit_person";
		}

		personDAO.changePerson(person, id);
		return "redirect:/people";
	}


	@DeleteMapping("/{id}")
	public String deletePerson(@PathVariable("id") int id) {
		personDAO.deletePerson(id);
		return "redirect:/people";
	}
}
