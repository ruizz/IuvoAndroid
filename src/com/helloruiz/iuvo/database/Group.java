package com.helloruiz.iuvo.database;

/**
 * Group class. Used for organizing the user's groups for classes.
 * This includes getter and setter methods to maintain a single group as an object.
 *
 */
public class Group {
	
	// private variables
	int _id; // unique
	String name; // unique
	
	@Override
    public String toString() {
      return name;
    }
	
	// empty constructor
	public Group() { }
	
	// constructor
	public Group(String name) {
		this.name = name;
	}
	
	// constructor with id
	public Group(int id, String name) {
		this._id = id;
		this.name = name;
	}
	
	// get id
	public int getID() {
		return this._id;
	}
	
	// set id
	public void setID(int id) {
		this._id = id;
	}	
	
	// get name
	public String getName() {
		return this.name;
	}
	
	// set name
	public void setName(String name) {
		this.name = name;
	}
}
