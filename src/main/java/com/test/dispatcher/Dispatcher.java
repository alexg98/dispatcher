package com.test.dispatcher;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @author alexg98@gmail.com
 *
 */
public final class Dispatcher {

	private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
	private ExecutorService executor; 
	private ReentrantLock lock = new ReentrantLock();
	private final static int RETRY_TIME = 2;
	private static Dispatcher instance;
	
	private Dispatcher() {		
		executor = Executors.newFixedThreadPool(10);
	}
	
	public static Dispatcher getInstance() {
		if(instance == null) {
			instance = new Dispatcher();
		}
		return instance;
	}
		
	/**
	 * Use of implementation of class CompletableFuture for manage the concurrency in the Dispatcher class
	 * @param call
	 */
	public boolean dispatch(Call call){		
		Optional<Employee> emp = AvailabilityEmployees.getEmployeeAvaible();
		if(emp.isPresent()) {			
			assignAndAnswerCall(call, emp.get());
		}else {
			callRetry(call);
		}
		return true;
	}
	
	/**
	 * Assign the employee to the incoming call and remove the employee 
	 * from the list of available employees.
	 * CompletableFuture 
	 * Start the process of attention of the call
	 * @param call
	 * @throws RuntimeException
	 */
	private void assignAndAnswerCall(Call call, Employee employee){	
		try {
			AvailabilityEmployees.removeFirstOccurrence(employee);
			CompletableFuture.supplyAsync(() -> 
				initCall(call, employee), executor)
			.whenCompleteAsync((emp, e) -> callFinished(emp));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * Simulates the attention of the call and 
	 * Releases the employee who received the call
	 * @param call
	 * @return employee available  
	 */
	private Employee initCall(Call call, Employee employee){
		call.setEmployee(employee);
		logger.info(String.format("Employee %s [%d] assign to call [%d] duration call %d .", 
				call.getEmployee().getTypeEmployee(), call.getEmployee().getId(), call.getId(), call.getDuration() ));
		Utilities.sleepSeconds(call.getDuration() );
		return call.getEmployee();
	}
	
	/**
	 * Ends call and frees employee for next call availability
	 * @param employee
	 */
	private void callFinished(Employee employee) {
		AvailabilityEmployees.addLast(employee);
		logger.info(String.format("Employee %s [%d] available ",employee.getTypeEmployee(), employee.getId() ));
	}
	
	/**
	 * In the event that not available employees try again after two seconds
	 * @param Call call
	 */
	private void callRetry(Call call) {
		CompletableFuture.supplyAsync(() -> {
			logger.info(String.format("Call waiting [%d] - %d s .", call.getId(), call.getDuration() ));
			Utilities.sleepSeconds(RETRY_TIME);
			dispatch(call);
			return true;
		}, executor);		
	}

	/**
	 * return instance ExecutorService
	 * @return
	 * @throws InterruptedException 
	 */
	public boolean awaitTermination(long timeout,TimeUnit unit) throws InterruptedException {
		return executor.awaitTermination(timeout, unit);
	}	
}
