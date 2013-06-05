package com.helloruiz.iuvo.database;

/**
 * Semester class. Used for organizing the user's semesters for classes.
 * This includes getter and setter methods to maintain a single semester as an object.
 *
 */
public class Semester {
	
	// private variables
	private int _id;
	private int referenceKey;
	private String name;
	private String color;
	
	@Override
    public String toString() {
      return name;
    }
	
	// empty constructor
	public Semester() { }
	
	// Constructor
	public Semester(int id, int referenceKey, String name, String color) {
		this._id = id;
		this.referenceKey = referenceKey;
		this.name = name;
		this.color = color;
	}
	
	// get id
	public int getID() {
		return this._id;
	}
	
	// set id
	public void setID(int id) {
		this._id = id;
	}
	
	// get reference key
	public int getReferenceKey() {
		return this.referenceKey;
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
