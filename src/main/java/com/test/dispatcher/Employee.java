package com.test.dispatcher;

/**
 * Class employee
 * @author alge3325
 *
 */
public class Employee {

	private TypeEmployee typeEmployee;
	private int id;
	
	public Employee(TypeEmployee typeEmployee, int id) {
		this.typeEmployee = typeEmployee;
		this.id = id;
	}
	
	public Employee(TypeEmployee typeEmployee) {
		this.typeEmployee = typeEmployee;
	}
	
	public TypeEmployee getTypeEmployee() {
		return typeEmployee;
	}

	public int getId() {
		return id;
	}	
}
