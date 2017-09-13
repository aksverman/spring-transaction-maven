package com.zetainteractive.security.test;

public class Person {
	private String firstName;
	private String lastName;
	private int age;
	
	Person(String fname) {
		this.firstName = fname;
	}
	Person(String fname, String lname) {
		this.firstName = fname;
		this.lastName = lname;
	}
	Person(String fname, int age) {
		this.firstName = fname;
		this.age = age;
	}
	Person(String fname, String lname, int age) {
		this.firstName = fname;
		this.lastName = lname;
		this.age = age;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "Person [firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + "]";
	}

	
}
