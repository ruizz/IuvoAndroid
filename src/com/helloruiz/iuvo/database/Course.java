package com.helloruiz.iuvo.database;

/**
 * Course class. Used for organizing the user's groups for classes.
 * This includes getter and setter methods to maintain a single course as an object.
 *
 */
public class Course {
	
	// private variables
	
	// unique, unlike group and semester, this will probably never change.
	// Essentially the same as 'referenceKey' in Group and Semester, except
	// that it doesn't need to be referenced by this.
	private int _id;
	
	// Used for course order within group. Only way to reference a course.
	private int groupID; 
	
	// Indicates what Group and/or Semester the course is tied to
	private int semesterReferenceKey; // -1 for none
	private int groupReferenceKey;
	
	private String name;
	private int hours;
	private String grade;
	
	// stored as int in database, 0 for false, 1 for true
	private int isCompleted;
	private int excludedFromGPA;
	private int excludedFromDegreePlan;
	
	// empty constructor
	public Course() { };
	
	// Constructor
	public Course(int id, int groupID, int semesterReferenceKey, int groupReferenceKey, 
			String name, int hours, String grade, int isCompleted,
			int excludedFromGPA, int excludedFromDegreePlan) {
		this._id = id;
		this.groupID = groupID;
		this.semesterReferenceKey = semesterReferenceKey;
		this.groupReferenceKey = groupReferenceKey;
		this.name = name;
		this.hours = hours;
		this.grade = grade;
		this.isCompleted = isCompleted;
		this.excludedFromGPA = excludedFromGPA;
		this.excludedFromDegreePlan = excludedFromDegreePlan;
	}
	
	// get id
	public int getID() {
		return this._id;
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
	
	// get isCompleted
	public int getIsCompleted() {
		return this.isCompleted;
	}
	
	// set isCompleted
	public void setIsCompleted(int isCompleted) {
		this.isCompleted = isCompleted;
	}	
	
	// get excluded from GPA
	public int getExcludedFromGPA() {
		return this.excludedFromGPA;
	}
	
	// set excluded from GPA
	public void setExcludedFromGPA(int excludedFromGPA) {
		this.excludedFromGPA = excludedFromGPA;
	}
	
	// get excluded from GPA
	public int getExcludedFromDegreePlan() {
		return this.excludedFromDegreePlan;
	}
	
	// set excluded from GPA
	public void setExcludedFromDegreePlan(int excludedFromDegreePlan) {
		this.excludedFromDegreePlan = excludedFromDegreePlan;
	}	
}
