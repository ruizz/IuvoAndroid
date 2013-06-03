package com.helloruiz.iuvo.database;

/**
 * Course class. Used for organizing the user's groups for classes.
 * This includes getter and setter methods to maintain a single course as an object.
 *
 */
public class Course {
	
	// private variables
	int _id; // unique, unlike group and semester, this will probably never change.
	int groupID; // used for course order within group.
	int semesterReferenceKey; // -1 for none
	int groupReferenceKey;
	String name;
	int hours;
	String grade;
	boolean excludedFromGPA; // stored as int in database, 0 for false, 1 for true
	
	// empty constructor
	public Course() { };
	
	// Constructor
	public Course(int id, int groupID, int semesterReferenceKey, int groupReferenceKey, 
			String name, int hours, String grade, boolean excludedFromGPA) {
		this._id = id;
		this.groupID = groupID;
		this.semesterReferenceKey = semesterReferenceKey;
		this.groupReferenceKey = groupReferenceKey;
		this.name = name;
		this.hours = hours;
		this.grade = grade;
		this.excludedFromGPA = excludedFromGPA;
	}
	
	// get id
		public int getID() {
			return this._id;
		}
		
	// set id, this shouldn't be needed. remove later if so.
	public void setID(int id) {
		this._id = id;
	}
	
	// get group ID
	public int getGroupID() {
		return this.groupID;
	}
	
	// set group ID
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}
	
	// get semester reference key
	public int getSemesterReferenceKey() {
		return this.semesterReferenceKey;
	}
	
	// set semester reference key
	public void setSemesterReferenceKey(int semesterReferenceKey) {
		this.semesterReferenceKey = semesterReferenceKey;
	}
	
	// get group reference key
	public int getGroupReferenceKey() {
		return this.groupReferenceKey;
	}
	
	// set semester reference key
	public void setGroupReferenceKey(int groupReferenceKey) {
		this.groupReferenceKey = groupReferenceKey;
	}
	
	// get name
	public String getName() {
		return this.name;
	}
	
	// set name
	public void setName(String name) {
		this.name = name;
	}
	
	// get hours
	public int getHours() {
		return this.hours;
	}
	
	// set hours
	public void setHours(int hours) {
		this.hours = hours;
	}
	
	// get grade
	public String getGrade() {
		return this.grade;
	}
	
	// set grade
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	// get excluded from GPA
	public boolean getExcludedFromGPA() {
		return this.excludedFromGPA;
	}
	
	// set excluded from GPA
	public void setExcludedFromGPA(boolean excludedFromGPA) {
		this.excludedFromGPA = excludedFromGPA;
	}
}
