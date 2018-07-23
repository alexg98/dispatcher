package com.test.dispatcher;

import java.util.LinkedList;
import java.util.Optional;
/**
 * Class AvailabilityEmployees
 * @author alexg98@gmail.com
 *
 */
public final class AvailabilityEmployees {

	private static LinkedList<Employee> listEmp;
	
	public static void loadEmployees(LinkedList<Employee> listEmp) {
		AvailabilityEmployees.listEmp = listEmp;
	}
	
	public static boolean removeFirstOccurrence(Employee emp) {
		return listEmp.removeFirstOccurrence(emp);
	}
	
	public static void addLast(Employee emp) {
		listEmp.addLast(emp);
	}

	public static int size() {
		return listEmp.size();
	}
	
	public static LinkedList<Employee> getAllEmployees() {
		return listEmp;
	}
	/**
	 * This method returns an employee that is available, which means that no call has been assigned. 
	 * the order of availability depends on the level of the employee, if there are no operators a 
	 * supervisor will be assigned and if no supervisors will assign a director.
	 * 
	 * 
	 * @return Optional<Employee>
	 * @throws RuntimeException
	 */
	public static synchronized  Optional<Employee> getEmployeeAvaible() throws RuntimeException{
		Optional<Employee> employee = listEmp.stream().filter(emp -> emp.getTypeEmployee().equals(TypeEmployee.OPERATOR )).findAny();
		if(!employee.isPresent()) {
			employee = listEmp.stream().filter(emp -> emp.getTypeEmployee().equals(TypeEmployee.SUPERVISOR )).findAny();
			if(!employee.isPresent()) {
				employee = listEmp.stream().filter(emp -> emp.getTypeEmployee().equals(TypeEmployee.DIRECTOR )).findAny();				
			}
		}		
		return employee;
	}
}
