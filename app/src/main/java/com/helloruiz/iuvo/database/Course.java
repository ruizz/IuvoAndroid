package com.helloruiz.iuvo.database;

/**
 * Course class. Used for organizing the user's groups for classes.
 * This includes getter and setter methods to maintain a single course as an object.
 *
 */
public class Course {
	
	// private variables
	private int _id; // unique, can never change
	private int position; // applies to position within the GROUP that it's assigned to.

	private String name;
	private int hours;
	private String grade;
	private int excludedFromGPA; // stored as int in database, 0 for false, 1 for true
	
	// Indicates what Group and/or Semester the course is tied to
	private int semesterID; // -1 for none
	private int groupID; // -1 to hide from degree plan
	
	// Constructor
	public Course(int id, int position, String name, int hours, String grade, int excludedFromGPA, int semesterID, int groupID) {
		this._id = id;
		this.position = position;
		this.name = name;
		this.hours = hours;
		this.grade = grade;
		this.excludedFromGPA = excludedFromGPA;
		this.semesterID = semesterID;
		this.groupID = groupID;

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
	
	// get semester ID
	public int getSemesterID() {
		return this.semesterID;
	}
	
	// set semester ID
	public void setSemesterID(int semesterID) {
		this.semesterID = semesterID;
	}
	
	// get group ID
	public int getGroupID() {
		return this.groupID;
	}
	
	// set group ID
	public void setGroupID(int groupID) {
		this.groupID = groupID;
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
	public int getExcludedFromGPA() {
		return this.excludedFromGPA;
	}
	
	// set excluded from GPA
	public void setExcludedFromGPA(int excludedFromGPA) {
		this.excludedFromGPA = excludedFromGPA;
	}	
}
