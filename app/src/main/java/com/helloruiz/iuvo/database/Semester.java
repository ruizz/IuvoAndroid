package com.helloruiz.iuvo.database;

/**
 * Semester class. Used for organizing the user's semesters for classes.
 * This includes getter and setter methods to maintain a single semester as an object.
 *
 */
public class Semester {
	
	// private variables
	private final int _id; // unique, can never change
	private int position;
	private String name;
	private String color;
	
	@Override
    public String toString() {
      return name;
    }
	
	// Constructor
	public Semester(int id, int position, String name, String color) {
		this._id = id;
		this.position = position;
		this.name = name;
		this.color = color;
	}
	
	// get id
	public int getID() {
		return this._id;
	}
	
	// get position
	public int getPosition() {
		return this.position;
	}
	
	// set position
	public void setPosition(int position) {
		this.position = position;
	}
	
	// get name
	public String getName() {
		return this.name;
	}
	
	// set name
	public void setName(String name) {
		this.name = name;
	}
	
	// get color
	public String getColor() {
		return this.color;
	}
	
	// set color
	public void setColor (String color) {
		this.color = color;
	}
}
