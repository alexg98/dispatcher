package com.test.dispatcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import java.util.LinkedList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class AvailabilityEmployeesTest {

	LinkedList<Employee> listEmp = new LinkedList<>();
	@Before
    public void executedBefore() {		
		listEmp.add(new Employee(TypeEmployee.OPERATOR));
		listEmp.add(new Employee(TypeEmployee.OPERATOR));
		listEmp.add(new Employee(TypeEmployee.OPERATOR));
		listEmp.add(new Employee(TypeEmployee.SUPERVISOR));
		listEmp.add(new Employee(TypeEmployee.SUPERVISOR));
		listEmp.add(new Employee(TypeEmployee.DIRECTOR));
    	AvailabilityEmployees.loadEmployees(listEmp); 
    }
	
	@Test
	public void getEmployeeAvaible(){
		Optional<Employee> employee = AvailabilityEmployees.getEmployeeAvaible();
		assertEquals(TypeEmployee.OPERATOR, employee.get().getTypeEmployee());
		//Remove OPERATOR
		listEmp.removeIf( emp -> emp.getTypeEmployee().equals(TypeEmployee.OPERATOR));		
		employee = AvailabilityEmployees.getEmployeeAvaible();
		assertNotEquals(TypeEmployee.OPERATOR, employee.get().getTypeEmployee());
		assertEquals(TypeEmployee.SUPERVISOR, employee.get().getTypeEmployee());		
		//Remove SUPERVISOR
		listEmp.removeIf( emp -> emp.getTypeEmployee().equals(TypeEmployee.SUPERVISOR));		
		employee = AvailabilityEmployees.getEmployeeAvaible();
		assertNotEquals(TypeEmployee.OPERATOR, employee.get().getTypeEmployee());
		assertNotEquals(TypeEmployee.SUPERVISOR, employee.get().getTypeEmployee());
		assertEquals(TypeEmployee.DIRECTOR, employee.get().getTypeEmployee());
		//Remove DIRECTOR		
		listEmp.removeIf( emp -> emp.getTypeEmployee().equals(TypeEmployee.DIRECTOR));
		employee = AvailabilityEmployees.getEmployeeAvaible();		
		assertFalse(employee.isPresent());		
	}	
}
