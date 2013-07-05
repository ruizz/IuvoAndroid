package com.helloruiz.iuvo.database;

/**
 * Group class. Used for organizing the user's groups for classes.
 * This includes getter and setter methods to maintain a single group as an object.
 *
 */
public class Group {
	
	// private variables
	private int _id; // unique, can never change
	private int position;
	private String name;
	
	@Override
    public String toString() {
      return name;
    }
	
	// empty constructor
	public Group() { }
	
	// constructor
	public Group(int id, int position, String name) {
		this._id = id;
		this.position = position;
		this.name = name;
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
}
