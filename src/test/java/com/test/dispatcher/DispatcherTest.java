package com.test.dispatcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

public class DispatcherTest {

    private static final int CANT_CALL = 20;
    private static final int MIN_CALL_DURATION = 5;
    private static final int MAX_CALL_DURATION = 10;
    private static final int CANT_EMPLOYEE = 10;
    private static Random rand = new Random();
    
   
    @Before
    public void executedBefore() {
    	AvailabilityEmployees.loadEmployees(buildEmployeeList()); 
    }
    
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
    
    @Test
    public void testDispatchWith10Calls() throws InterruptedException {               
        try {
        	ExecutorService executor = Executors.newWorkStealingPool();
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
        	
        	executor.invokeAll(callables);      	        	
        	executor.awaitTermination(MAX_CALL_DURATION * 2, TimeUnit.SECONDS);        	
		} catch (Exception e) {
			fail(e.getMessage());
		}        
        assertEquals(CANT_EMPLOYEE, AvailabilityEmployees.size());
    }

    private static LinkedList<Employee> buildEmployeeList() {    	
    	TypeEmployee[] types = {TypeEmployee.OPERATOR, TypeEmployee.SUPERVISOR, TypeEmployee.DIRECTOR};
    	   	
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
