package com.saien.cglib;

/**
 * @author Paiker Abbas
 * 
 *         This is the example for CGLib
 *
 */
public class PersonService {

	public String sayHello(String name) {
		return "Hello" + name;
	}

	public Integer nameLength(String name) {
		return name.length();
	}
}
