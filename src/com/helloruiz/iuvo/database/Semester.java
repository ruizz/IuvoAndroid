package com.helloruiz.iuvo.database;

/**
 * Semester class. Used for organizing the user's semesters for classes.
 * This includes getter and setter methods to maintain a single semester as an object.
 *
 */
public class Semester {
	
	// private variables
	int term;
	int year;
	int color; // unique
	
	// empty constructor
	public Semester() { }
	
	public Semester(int term, int year, int color) {
		this.term = term;
		this.year = year;
		this.color = color;
	}
	
	public int getTerm() {
		return this.term;
	}
	
	public void setTerm(int term) {
		this.term = term;
	}
	
	public int getYear() {
		return this.year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public int getColor() {
		return this.color;
	}
	
	public void setColor (int color) {
		this.color = color;
	}
	
	
	
}
