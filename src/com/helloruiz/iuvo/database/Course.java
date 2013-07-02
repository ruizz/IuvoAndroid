package com.helloruiz.iuvo.database;

/**
 * Course class. Used for organizing the user's groups for classes.
 * This includes getter and setter methods to maintain a single course as an object.
 *
 */
public class Course {
	
	// private variables
	
	// Same function as Semester and Group, but with two key differences.
	// - NOT Unique
	// - Used to indicate the order within a GROUP.
	private int _id;
	
	// Indicates what Group and/or Semester the course is tied to
	private int semesterReferenceKey; // -1 for none
	private int groupReferenceKey; // -1 to hide from degree plan
	
	private String name;
	private int hours;
	private String grade;
	
	// stored as int in database, 0 for false, 1 for true
	private int excludedFromGPA;
	
	// empty constructor
	public Course() { };
	
	// Constructor
	public Course(int id, int semesterReferenceKey, int groupReferenceKey, 
			String name, int hours, String grade, int excludedFromGPA) {
		this._id = id;
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
	
	// set id
	public void setGroupID(int id) {
		this._id = id;
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
	
	// set group reference key
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
	public int getExcludedFromGPA() {
		return this.excludedFromGPA;
	}
	
	// set excluded from GPA
	public void setExcludedFromGPA(int excludedFromGPA) {
		this.excludedFromGPA = excludedFromGPA;
	}	
}
