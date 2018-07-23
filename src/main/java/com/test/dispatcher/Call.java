package com.test.dispatcher;
/**
 * Class Call
 * @author alexg98@gmail.com
 *
 */
public class Call {

	private int id;
	
	private int duration;
	
	private Employee employee;
	
	public Call(int duration, int id){
		this.duration = duration;
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public Employee getEmployee() {
		return employee;
	}
	
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}	
}
