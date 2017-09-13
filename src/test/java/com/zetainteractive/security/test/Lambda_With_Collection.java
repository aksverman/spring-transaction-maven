package com.zetainteractive.security.test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Lambda_With_Collection {

	public static void main(String[] args) {
		
		List<Person> persons = Arrays.asList(new Person("Anil",23), new Person("Venu",28), new Person("Ruhi",19));
		persons.forEach(p -> p.setLastName("Zeta"));
		
		// sequential version
		long noOfPersonsOver20 = persons.stream().filter(p -> p.getAge() > 20).count();
		List<Person> personsOver20 = persons.stream().filter(p -> p.getAge() > 20).collect(Collectors.toList());
		System.out.println("No of persons qualifying age limit : " + noOfPersonsOver20 +
							"\nPersons qualified are : " + personsOver20);
	}
}
