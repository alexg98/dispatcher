package com.test.dispatcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

public class DispatcherTest {

    private static final int CANT_CALL = 15;
    private static final int MIN_CALL_DURATION = 5;
    private static final int MAX_CALL_DURATION = 10;
    private static final int CANT_EMPLOYEE = 10;
    private static Random rand = new Random();
    
   
    @Before
    public void executedBefore() {
    	AvailabilityEmployees.loadEmployees(buildEmployeeList()); 
    }
    /**
     * see trace log
     * @throws InterruptedException
     */
    @Test
    public void testDispatch() throws InterruptedException {               
        try {
        	Dispatcher dispatcher = Dispatcher.getInstance();
        	showEmployess();
        	buildCallList().forEach( call -> dispatcher.dispatch(call) );        	
        	dispatcher.awaitTermination(MAX_CALL_DURATION * 2, TimeUnit.SECONDS);        	
		} catch (Exception e) {
			fail(e.getMessage());
		}        
        assertEquals(CANT_EMPLOYEE, AvailabilityEmployees.size());
    }
    /**
     * see trace log
     * @throws InterruptedException
     */
    @Test
    public void employeeNotAvailableTest() throws InterruptedException {    	
    	LinkedList<Employee> listEmployees = new LinkedList<>();
    	AvailabilityEmployees.loadEmployees(listEmployees);    	
    	//There are not employees available
    	Dispatcher dispatcher = Dispatcher.getInstance();
    	dispatcher.dispatch(new Call(5, 1));
    	assertTrue(AvailabilityEmployees.getAllEmployees().isEmpty());
    	Utilities.sleepSeconds(5);    	
    	AvailabilityEmployees.addLast(new Employee(TypeEmployee.OPERATOR, 1));
    	//There is a employee available
    	assertTrue(AvailabilityEmployees.getAllEmployees().size() == 1);    	
    	dispatcher.awaitTermination(MAX_CALL_DURATION, TimeUnit.SECONDS); 
    }
    /**
     * see trace log
     * @throws InterruptedException
     */    
    @Test
    public void testDispatchWith10Calls() throws InterruptedException {    	
        try {        	
        	Dispatcher dispatcher = Dispatcher.getInstance();
        	List<Call> calls = buildCallList();        	        	
        	List<Callable<Boolean>> callables = Arrays.asList(
        	    () -> dispatcher.dispatch(calls.get(1)),
        	    () -> dispatcher.dispatch(calls.get(2)),
        	    () -> dispatcher.dispatch(calls.get(3)),
        	    () -> dispatcher.dispatch(calls.get(4)),
        	    () -> dispatcher.dispatch(calls.get(5)),
        	    () -> dispatcher.dispatch(calls.get(6)),
        	    () -> dispatcher.dispatch(calls.get(7)),
        	    () -> dispatcher.dispatch(calls.get(8)),
        	    () -> dispatcher.dispatch(calls.get(9)),
        	    () -> dispatcher.dispatch(calls.get(10)));
        	
        	dispatcher.invokeAll(callables);      	        	
        	dispatcher.awaitTermination(MAX_CALL_DURATION * 2, TimeUnit.SECONDS);	        	
		} catch (Exception e) {
			fail(e.getMessage());
		}           
        assertEquals(CANT_EMPLOYEE, AvailabilityEmployees.size());
    }

    private static LinkedList<Employee> buildEmployeeList() {    	
    	TypeEmployee[] types = TypeEmployee.values();
    	   	
    	LinkedList<Employee> listEmp = new LinkedList<>();
    	for(int i = 0; i < CANT_EMPLOYEE ; i++) {
    		listEmp.add( new Employee(types[ rand.nextInt( types.length ) ], i ) );
    	}
    	return listEmp;
    }   
    
    private static List<Call> buildCallList() {
    	List<Call> listCalls = new ArrayList<>();
    	for(int i = 0; i < CANT_CALL ; i++) {
    		int duration = ThreadLocalRandom.current().nextInt(MIN_CALL_DURATION, MAX_CALL_DURATION + 1);
    		listCalls.add( new Call(duration,i) );  		
    	}    	
        return listCalls;
    }
    
    private void showEmployess(){
    	AvailabilityEmployees.getAllEmployees().stream().forEach( emp -> {
    		System.out.println( String.format(" Empleado [%d] type %s.", emp.getId(), emp.getTypeEmployee()));
    	});
    }
}
