package ru.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.library.models.Person;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {
//	private final PersonDAO personDAO;
	private final PeopleService peopleService;

	@Autowired
	public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
//        this.personDAO = personDAO;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Person.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Person person = (Person) target;
		Optional<Person> foundPerson = peopleService.findPersonByFullName(person.getName());

//		System.out.println("Person " + person.getPersonId());
//		System.out.println("Found person " + foundPerson.get().getPersonId());

		if (foundPerson.isPresent() && person.getPersonId() != foundPerson.get().getPersonId()) {

			errors.rejectValue("name", "", "This full name is already taken!");
		}
	}
}
