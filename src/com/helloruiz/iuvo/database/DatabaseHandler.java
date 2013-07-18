package com.helloruiz.iuvo.database;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

/**
 * Database Handler for all group, semester, and class databases. Based on:
 * http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
 * 
 * Handler operations assume that the positions for group, semester, or course items are sequential from 0 with no gaps.
 * ex. 0, 1, 2 or 0 1, 2, 3, 4, 5, 6
 */
public class DatabaseHandler extends SQLiteOpenHelper {

	/**
	 * -- Variables --
	 */
	
	// Database
	SQLiteDatabase db;
	
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "IuvoDatabase.db";
 
    // Group table name
    private static final String TABLE_GROUP = "groups";
    private static final String TABLE_SEMESTER = "semesters";
    private static final String TABLE_COURSE = "courses";
 
    // Group Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_POSITION = "position";
    private static final String KEY_COLOR = "color";
    private static final String KEY_SEMESTER_ID = "semesterID";
    private static final String KEY_GROUP_ID = "groupID";
    private static final String KEY_HOURS = "hours";
    private static final String KEY_GRADE = "grade";
    private static final String KEY_EXCLUDED_FROM_GPA = "excludedFromGPA";

    // Database location in file system.
    public static String DB_FILEPATH;
    
    /**
     * Constructor
     */
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DB_FILEPATH = context.getDatabasePath("IuvoDatabase.db").toString();
        db = this.getWritableDatabase();
    }
    
    /**
	 * -- Overrides --
	 */
    // Called only when getWritableDatabase() or getReadableDatabase() is called.
    // In this case, called only in the constructor.
    @Override
    public void onCreate(SQLiteDatabase db) {
    	
    	// Group
        String CREATE_GROUP_TABLE = "create table " + TABLE_GROUP + "("
        		+ KEY_ID + " INTEGER PRIMARY KEY,"
        		+ KEY_POSITION + " INTEGER,"
                + KEY_NAME + " TEXT" + ")";
        
        // Table
        String CREATE_SEMESTER_TABLE = "create table " + TABLE_SEMESTER + "("
        		+ KEY_ID + " INTEGER PRIMARY KEY,"
        		+ KEY_POSITION + " INTEGER,"
                + KEY_NAME + " TEXT," 
        		+ KEY_COLOR + " TEXT" + ")";
        
        // Course
        String CREATE_COURSE_TABLE = "create table " + TABLE_COURSE + "("
        		+ KEY_ID + " INTEGER,"
        		+ KEY_POSITION + " INTEGER,"
        		+ KEY_NAME + " TEXT,"
        		+ KEY_HOURS + " INTEGER,"
        		+ KEY_GRADE + " TEXT,"
        		+ KEY_EXCLUDED_FROM_GPA + " INTEGER,"
        		+ KEY_SEMESTER_ID + " INTEGER,"
        		+ KEY_GROUP_ID + " INTEGER" + ")";
        
        db.execSQL(CREATE_GROUP_TABLE);
        db.execSQL(CREATE_SEMESTER_TABLE);
        db.execSQL(CREATE_COURSE_TABLE);

    }
 
    // I'll worry about this if I ever have to upgrade the database. Leave clear for now.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { ; }
    
    /**
	 * -- Group Operations --
	 */
    // Add new group
    public void addGroup(String groupName) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, getMaxGroupID() + 1);
        values.put(KEY_POSITION, getGroupCount());
        values.put(KEY_NAME, groupName);
     
        // Adding Row
        db.insert(TABLE_GROUP, null, values);
        ;
    }
    
    // Move group position in list.
    public void moveGroup(int from, int to) {
    	ContentValues values;
    	
    	if (from != to) {
    		
    		// Temporarily set moving group position to -1
    		values = new ContentValues();
    		values.put(KEY_POSITION, -1);
    		db.update(TABLE_GROUP, values, KEY_POSITION + "=?", new String[] {String.valueOf(from)} );
    		
    		if (from > to) {
    			// Increment other groups
        		for (int i = from - 1; i >= to; i--) {
        			values = new ContentValues();
        			values.put(KEY_POSITION, i + 1);
        			db.update(TABLE_GROUP, values, KEY_POSITION + "=?", new String[] {String.valueOf(i)});
        		}
    		} else if (to > from) {
    			// Decrement other groups
        		for (int i = from + 1; i <= to; i++) {
        			values = new ContentValues();
        			values.put(KEY_POSITION, i - 1);
        			db.update(TABLE_GROUP, values, KEY_POSITION + "=?", new String[] {String.valueOf(i)});
        		}
    		}

    		// Set moving group to destination position
    		values = new ContentValues();
    		values.put(KEY_POSITION, to);
    		db.update(TABLE_GROUP, values, KEY_POSITION + "=?", new String[] {String.valueOf(-1)} );
    	}
    }
    
    // Get single group
    public Group getGroup(int id) {
    	
        Cursor cursor = db.query(TABLE_GROUP, new String[] {KEY_ID, KEY_POSITION, KEY_NAME}, KEY_ID + "=?", new String[] { String.valueOf( id ) },
        		null, null, null, null);
        
        if (cursor != null)
            cursor.moveToFirst();
     
        Group group = new Group(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2));
        
        cursor.close();
        
        // return group
        return group;
    }
    
    // Get single group
    public Group getGroupByPosition(int position) {
     
        Cursor cursor = db.query(TABLE_GROUP, new String[] {KEY_ID, KEY_POSITION, KEY_NAME}, KEY_POSITION + "=?", new String[] { String.valueOf( position ) },
        		null, null, null, null);
        
        if (cursor != null)
            cursor.moveToFirst();
     
        Group group = new Group(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2));
        
        cursor.close();
                
        // return group
        return group;
    }
    
    // Get all groups. Ordered by position.
    public List<Group> getAllGroups() {
        List<Group> groupList = new ArrayList<Group>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GROUP + " ORDER BY " + KEY_POSITION;
     
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Group group = new Group(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2));
                
                // Adding group to list
                groupList.add(group);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
     
        // return group list
        return groupList;
    }
    
    // Update single group name.
    public void updateGroup(Group group) {
     
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, group.getName());
     
        // updating row
        db.update(TABLE_GROUP, values, KEY_ID + " = ?", new String[] { String.valueOf(group.getID()) });
        ;
    }
    
    // Delete single group 
    public void deleteGroup(Group group) {
    	
    	// Group size after deletion of the group.
    	int groupCount = getGroupCount() - 1;
    	
    	// Size of hidden group.
    	int hiddenGroupCount = getCourseCountByGroup(-1);
    	
        ContentValues values;
        
        // Update all courses associated with this semester. First the position...
        List<Course> courses = getAllCoursesByGroup(group.getID());
	    for (Course c : courses) {
	    	values = new ContentValues();
	    	values.put(KEY_POSITION,String.valueOf(c.getPosition() + hiddenGroupCount));
	    	db.update(TABLE_COURSE, values, KEY_ID + " = ?", new String[] {String.valueOf(c.getID())});
	    }
        
	    // ... and then the groupID
		values = new ContentValues();
		values.put(KEY_GROUP_ID, "-1");
	    db.update(TABLE_COURSE, values, KEY_GROUP_ID + " = ?", new String[] {String.valueOf(group.getID())});
	    
        
        // Delete the group
        db.delete(TABLE_GROUP, KEY_ID + " = ?",new String[] { String.valueOf(group.getID()) });
        
        // Decrement all of the groups below the group that was deleted
        for (int i = group.getPosition() + 1; i <= groupCount; i++) {
			values = new ContentValues();
			values.put(KEY_POSITION, i - 1);
			db.update(TABLE_GROUP, values, KEY_POSITION + "=?", new String[] {String.valueOf(i)});
		}
    }

	// Get group count
	public int getGroupCount() {
		
	    String countQuery = "SELECT * FROM " + TABLE_GROUP;
	    Cursor cursor = db.rawQuery(countQuery, null);
	    
	    int count = cursor.getCount();
	    cursor.close();
	    
	    // return count
	    return count;
	}
	
	// Get max group ID.
	public int getMaxGroupID() {
		if (getGroupCount() == 0)
			return -1;
		
		// Get max group ID from database.
        String maxGroupIDQuery = "SELECT MAX(" + KEY_ID + ") AS maxGroupID FROM " + TABLE_GROUP;
        
        Cursor cursor = db.rawQuery(maxGroupIDQuery, null);
        
        if (cursor != null)
            cursor.moveToFirst();
        
        int result = cursor.getInt(0);
        Log.d("MaxReferenceKey: ", "Result: " + result);
        
        cursor.close();
        
		return result;
	}
	
	/**
	 * -- Semester Operations --
	 */
    // Add new semester
    public void addSemester(String semesterName, String semesterColor) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, getMaxSemesterID() + 1);
        values.put(KEY_POSITION, getSemesterCount());
        values.put(KEY_NAME, semesterName);
		values.put(KEY_COLOR, semesterColor);
     
        // Adding Row
        db.insert(TABLE_SEMESTER, null, values);
    }
    
    // Move semester position in list.
    public void moveSemester(int from, int to) {
    	ContentValues values;
    	
    	if (from != to) {
    		
    		// Temporarily set moving semester position to -1
    		values = new ContentValues();
    		values.put(KEY_POSITION, -1);
    		db.update(TABLE_SEMESTER, values, KEY_POSITION + "=?", new String[] {String.valueOf(from)} );
    		
    		if (from > to) {
    			// Increment other semesters
        		for (int i = from - 1; i >= to; i--) {
        			values = new ContentValues();
        			values.put(KEY_POSITION, i + 1);
        			db.update(TABLE_SEMESTER, values, KEY_POSITION + "=?", new String[] {String.valueOf(i)});
        		}
    		} else if (to > from) {
    			// Decrement other semesters
        		for (int i = from + 1; i <= to; i++) {
        			values = new ContentValues();
        			values.put(KEY_POSITION, i - 1);
        			db.update(TABLE_SEMESTER, values, KEY_POSITION + "=?", new String[] {String.valueOf(i)});
        		}
    		}

    		// Set moving semester to destination position
    		values = new ContentValues();
    		values.put(KEY_POSITION, to);
    		db.update(TABLE_SEMESTER, values, KEY_POSITION + "=?", new String[] {String.valueOf(-1)} );
    	}
    }
    
    // Get single semester
    public Semester getSemester(int id) {
     
        Cursor cursor = db.query(TABLE_SEMESTER, new String[] {KEY_ID, KEY_POSITION, KEY_NAME, KEY_COLOR}, KEY_ID + "=?", new String[] { String.valueOf( id ) },
        		null, null, null, null);
        
        if (cursor != null)
            cursor.moveToFirst();
     
        Semester semester = new Semester(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3));
        
        cursor.close();
        
        // return semester
        return semester;
    }
    
    // Get single semester by position
    public Semester getSemesterByPosition(int position) {
     
        Cursor cursor = db.query(TABLE_SEMESTER, new String[] {KEY_ID, KEY_POSITION, KEY_NAME, KEY_COLOR}, KEY_POSITION + "=?", new String[] { String.valueOf( position ) },
        		null, null, null, null);
        
        if (cursor != null)
            cursor.moveToFirst();
     
        Semester semester = new Semester(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3));
        
        cursor.close();
        
        // return semester
        return semester;
    }
    
    // Get all semesters. Ordered by position.
    public List<Semester> getAllSemesters() {
        List<Semester> semesterList = new ArrayList<Semester>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SEMESTER + " ORDER BY " + KEY_POSITION;
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Semester semester = new Semester(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3));
                
                // Adding semester to list
                semesterList.add(semester);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
     
        // return semester list
        return semesterList;
    }
    
    // Update single semester name and/or color.
    public void updateSemester(Semester semester) {
	 
        ContentValues values = new ContentValues();
        
		values.put(KEY_NAME, semester.getName());
		values.put(KEY_COLOR, semester.getColor());
     
        // updating row
        db.update(TABLE_SEMESTER, values, KEY_ID + " = ?", new String[] { String.valueOf(semester.getID()) });
        ;
    }
    
    // Delete single semester 
    public void deleteSemester(Semester semester) {
    	
    	int semesterCount = getSemesterCount() - 1;
        
        // Update all courses associated with this semester
		ContentValues values = new ContentValues();
		values.put(KEY_SEMESTER_ID, "-1");
        db.update(TABLE_COURSE, values, KEY_SEMESTER_ID + " = ?", new String[] {String.valueOf(semester.getID())});
        
        // Delete the semester
        db.delete(TABLE_SEMESTER, KEY_ID + " = ?",new String[] { String.valueOf(semester.getID()) });
        
        // Decrement all of the semesters below the semester that was deleted
        for (int i = semester.getPosition() + 1; i <= semesterCount; i++) {
			values = new ContentValues();
			values.put(KEY_POSITION, i - 1);
			db.update(TABLE_SEMESTER, values, KEY_POSITION + "=?", new String[] {String.valueOf(i)});
		}
    }

	// Get semester count
	public int getSemesterCount() {
		
	    String countQuery = "SELECT * FROM " + TABLE_SEMESTER;
	    Cursor cursor = db.rawQuery(countQuery, null);
	    
	    int count = cursor.getCount();
	    cursor.close();
	    
	    // return count
	    return count;
	}
	
	// Get max semester ID.
	public int getMaxSemesterID() {
		if (getSemesterCount() == 0)
			return -1;
		
		// Get max semester ID from database.
        String maxSemesterIDQuery = "SELECT MAX(" + KEY_ID + ") AS maxSemesterID FROM " + TABLE_SEMESTER;
        
        Cursor cursor = db.rawQuery(maxSemesterIDQuery, null);
        
        if (cursor != null)
            cursor.moveToFirst();
        
        int result = cursor.getInt(0);
        Log.d("MaxReferenceKey: ", "Result: " + result);
        
        cursor.close();
        
		return result;
	}
	
	/**
	 * -- Course Operations --
	 */
    // Add new course
    public void addCourse(String name, int hours, String grade, int excludedFromGPA, int semesterID, int groupID) {
        
    	ContentValues values = new ContentValues();
        values.put(KEY_ID, getMaxCourseID() + 1);
        values.put(KEY_POSITION, getCourseCountByGroup(groupID));
        values.put(KEY_NAME, name);
        values.put(KEY_HOURS, hours);
        values.put(KEY_GRADE, grade);
        values.put(KEY_EXCLUDED_FROM_GPA, excludedFromGPA);
        values.put(KEY_SEMESTER_ID, semesterID);
        values.put(KEY_GROUP_ID, groupID);
     
        // Adding Row
        db.insert(TABLE_COURSE, null, values);
        ;
    }
    
    // Move course position in list. Position applies to group that the course is in.
    public void moveCourse(int from, int to, int groupID) {
    	ContentValues values;
    	
    	if (from != to) {
    		
    		// Temporarily set moving course position to -1
    		values = new ContentValues();
    		values.put(KEY_POSITION, -1);
    		db.update(TABLE_COURSE, values, KEY_POSITION + "=? AND " + KEY_GROUP_ID + "=?", new String[] {String.valueOf(from), String.valueOf(groupID)} );
    		
    		if (from > to) {
    			// Increment other courses
        		for (int i = from - 1; i >= to; i--) {
        			values = new ContentValues();
        			values.put(KEY_POSITION, i + 1);
        			db.update(TABLE_COURSE, values, KEY_POSITION + "=? AND " + KEY_GROUP_ID + "=?", new String[] {String.valueOf(i), String.valueOf(groupID)});
        		}
    		} else if (to > from) {
    			// Decrement other courses
        		for (int i = from + 1; i <= to; i++) {
        			values = new ContentValues();
        			values.put(KEY_POSITION, i - 1);
        			db.update(TABLE_COURSE, values, KEY_POSITION + "=? AND " + KEY_GROUP_ID + "=?", new String[] {String.valueOf(i), String.valueOf(groupID)});
        		}
    		}

    		// Set moving course to destination position
    		values = new ContentValues();
    		values.put(KEY_POSITION, to);
    		db.update(TABLE_COURSE, values, KEY_POSITION + "=? AND " + KEY_GROUP_ID + "=?", new String[] {String.valueOf(-1), String.valueOf(groupID)} );
    	}
    }
    
    // Decrements the positions of courses in a group by 1.
    // Used for when moving classes between groups.
    public void decrementCoursePositions(int startingPosition, int groupID) {
    	ContentValues values;
    	
    	// Decrement other courses
		for (int i = startingPosition; i <= getCourseCountByGroup(groupID); i++) {
			values = new ContentValues();
			values.put(KEY_POSITION, i - 1);
			db.update(TABLE_COURSE, values, KEY_POSITION + "=? AND " + KEY_GROUP_ID + "=?", new String[] {String.valueOf(i), String.valueOf(groupID)});
		}
    }
    
    // Get single course
    public Course getCourse(int id) {
     
        Cursor cursor = db.query(TABLE_COURSE, 
        		new String[] {KEY_ID, KEY_POSITION, KEY_NAME, KEY_HOURS, KEY_GRADE, KEY_EXCLUDED_FROM_GPA, KEY_SEMESTER_ID, KEY_GROUP_ID},
        		KEY_ID + "=?",
        		new String[] { String.valueOf( id )},
        		null, null, null, null);
        
        if (cursor != null)
            cursor.moveToFirst();
     
        Course course = new Course(
        		Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),
        		cursor.getString(2), Integer.parseInt(cursor.getString(3)), cursor.getString(4),
        		Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)),
        		Integer.parseInt(cursor.getString(7))
        		);
        
        cursor.close();
        
        // return course
        return course;
    }
    
    // Get single course by position
    public Course getCourseByPosition(int position, int groupID) {
     
        Cursor cursor = db.query(TABLE_COURSE, 
        		new String[] {KEY_ID, KEY_POSITION, KEY_NAME, KEY_HOURS, KEY_GRADE, KEY_EXCLUDED_FROM_GPA, KEY_SEMESTER_ID, KEY_GROUP_ID},
        		KEY_POSITION + "=? AND " + KEY_GROUP_ID + "=?",
        		new String[] { String.valueOf( position ), String.valueOf(groupID)},
        		null, null, null, null);
        
        if (cursor != null)
            cursor.moveToFirst();
     
        Course course = new Course(
        		Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),
        		cursor.getString(2), Integer.parseInt(cursor.getString(3)), cursor.getString(4),
        		Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)),
        		Integer.parseInt(cursor.getString(7))
        		);
        
        cursor.close();
        
        // return course
        return course;
    }
    
    // Get all courses. Ordered by ID.
    public List<Course> getAllCourses() {
        List<Course> courseList = new ArrayList<Course>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COURSE + " ORDER BY " + KEY_ID;
     
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Course course = new Course(
                		Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),
                		cursor.getString(2), Integer.parseInt(cursor.getString(3)), cursor.getString(4),
                		Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)),
                		Integer.parseInt(cursor.getString(7))
                		);
                
                // Adding course to list
                courseList.add(course);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
     
        // return course list
        return courseList;
    }
    
    // Get all courses. Ordered by ID.
    public List<Course> getAllCoursesNotExcludedFromGPA() {
        List<Course> courseList = new ArrayList<Course>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COURSE + " WHERE " + KEY_EXCLUDED_FROM_GPA + "=0" + " ORDER BY " + KEY_ID;
     
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Course course = new Course(
                		Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),
                		cursor.getString(2), Integer.parseInt(cursor.getString(3)), cursor.getString(4),
                		Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)),
                		Integer.parseInt(cursor.getString(7))
                		);
                
                // Adding course to list
                courseList.add(course);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
     
        // return course list
        return courseList;
    }
    
    // Get all courses by group. Ordered by position.
    public List<Course> getAllCoursesByGroup(int groupID) {
        List<Course> courseList = new ArrayList<Course>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_COURSE + " WHERE " + KEY_GROUP_ID + "=" + String.valueOf(groupID) + " ORDER BY " + KEY_POSITION;
     
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Course course = new Course(
                		Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),
                		cursor.getString(2), Integer.parseInt(cursor.getString(3)), cursor.getString(4),
                		Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)),
                		Integer.parseInt(cursor.getString(7))
                		);
                
                // Adding course to list
                courseList.add(course);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
     
        // return course list
        return courseList;
    }
    
    // Get all courses by semester. Ordered by position.
    public List<Course> getAllCoursesBySemester(int semesterID) {
        List<Course> courseList = new ArrayList<Course>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_COURSE + " WHERE " + KEY_SEMESTER_ID + "=" + String.valueOf(semesterID) + " ORDER BY " + KEY_GROUP_ID;
     
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Course course = new Course(
                		Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),
                		cursor.getString(2), Integer.parseInt(cursor.getString(3)), cursor.getString(4),
                		Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)),
                		Integer.parseInt(cursor.getString(7))
                		);
                
                // Adding course to list
                courseList.add(course);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
     
        // return course list
        return courseList;
    }

    // Update single course info
    public void updateCourse(Course course) {
	 
        ContentValues values = new ContentValues();
        
        values.put(KEY_POSITION, course.getPosition());
		values.put(KEY_NAME, course.getName());
		values.put(KEY_HOURS, course.getHours());
        values.put(KEY_GRADE, course.getGrade());
        values.put(KEY_EXCLUDED_FROM_GPA, course.getExcludedFromGPA());
        values.put(KEY_SEMESTER_ID, course.getSemesterID());
        values.put(KEY_GROUP_ID, course.getGroupID());
        
        // updating row
        db.update(TABLE_COURSE, values, KEY_ID + " = ?", new String[] { String.valueOf(course.getID()) });
        ;
    }
    
    // Delete single course 
    public void deleteCourse(Course course) {
    	
    	int courseCount = getCourseCountByGroup(course.getGroupID()) - 1;
        
        // Delete the course
        db.delete(TABLE_COURSE, KEY_ID + " = ?",new String[] { String.valueOf(course.getID()) });
        
        // Decrement all of the courses below the course that was deleted
        for (int i = course.getPosition() + 1; i <= courseCount; i++) {
			ContentValues values = new ContentValues();
			values.put(KEY_POSITION, i - 1);
			db.update(TABLE_COURSE, values, KEY_POSITION + "=? AND " + KEY_GROUP_ID + "=?", new String[] {String.valueOf(i), String.valueOf(course.getGroupID())});
		}
    }

	// Get course count
	public int getCourseCount() {
		
	    String countQuery = "SELECT * FROM " + TABLE_COURSE;
	    Cursor cursor = db.rawQuery(countQuery, null);
	    
	    int count = cursor.getCount();
	    cursor.close();
	    
	    // return count
	    return count;
	}
	
	// Get course count by group ID
	public int getCourseCountByGroup(int groupID) {
		
	    String countQuery = "SELECT * FROM " + TABLE_COURSE + " WHERE " + KEY_GROUP_ID + "=" + String.valueOf(groupID);
	    Cursor cursor = db.rawQuery(countQuery, null);
	    
	    int count = cursor.getCount();
	    cursor.close();
	    
	    // return count
	    return count;
	}
	
	// Get course count by group position.
	public int getCourseCountByGroupPosition(int groupPosition) {
		
		int groupID = getGroupByPosition(groupPosition).getID();
		
	    String countQuery = "SELECT * FROM " + TABLE_COURSE + " WHERE " + KEY_GROUP_ID + "=" + String.valueOf(groupID);
	    Cursor cursor = db.rawQuery(countQuery, null);
	    
	    int count = cursor.getCount();
	    cursor.close();
	    
	    // return count
	    return count;
	}
	
	// Get course count by semester ID
	public int getCourseCountBySemester(int semesterID) {
		
	    String countQuery = "SELECT * FROM " + TABLE_COURSE + " WHERE " + KEY_SEMESTER_ID + "=" + String.valueOf(semesterID);
	    Cursor cursor = db.rawQuery(countQuery, null);
	    
	    int count = cursor.getCount();
	    cursor.close();
	    
	    // return count
	    return count;
	}
	
	// Get course count by semester
	public int getCourseCountBySemesterPosition(int semesterPosition) {
		
		int semesterID = getSemesterByPosition(semesterPosition).getID();
		
	    String countQuery = "SELECT * FROM " + TABLE_COURSE + " WHERE " + KEY_SEMESTER_ID + "=" + String.valueOf(semesterID);
	    Cursor cursor = db.rawQuery(countQuery, null);
	    
	    int count = cursor.getCount();
	    cursor.close();
	    
	    // return count
	    return count;
	}
	
	// Get course count in degree plan.
	public int getCourseCountInDegreePlan() {
		
	    String countQuery = "SELECT * FROM " + TABLE_COURSE + " WHERE " + KEY_GROUP_ID + "!=-1";
	    Cursor cursor = db.rawQuery(countQuery, null);
	    
	    int count = cursor.getCount();
	    cursor.close();
	    
	    // return count
	    return count;
	}
	
	// Get completed course count in degree plan.
	public int getCourseCountInDegreePlanCompleted() {
		
	    String countQuery = "SELECT * FROM " + TABLE_COURSE + " WHERE " + KEY_GROUP_ID + "!=? AND " + KEY_GRADE + "!=? AND " + KEY_GRADE + "!=?";
	    Cursor cursor = db.rawQuery(countQuery, new String[] {"-1", "None", "F "});
	    
	    int count = cursor.getCount();
	    cursor.close();
	    
	    // return count
	    return count;
	}
	
	// Get completed course count in degree plan.
	public int getCourseCountInDegreePlanAttempted() {
		
	    String countQuery = "SELECT * FROM " + TABLE_COURSE + " WHERE " + KEY_GROUP_ID + "!=? AND " + KEY_GRADE + "!=?";
	    Cursor cursor = db.rawQuery(countQuery, new String[] {"-1", "None"});
	    
	    int count = cursor.getCount();
	    cursor.close();
	    
	    // return count
	    return count;
	}
	
	// Get max course ID.
	public int getMaxCourseID() {
		if (getCourseCount() == 0)
			return -1;
		
		// Get max course ID from database.
        String maxCourseIDQuery = "SELECT MAX(" + KEY_ID + ") AS maxCourseID FROM " + TABLE_COURSE;
        
        Cursor cursor = db.rawQuery(maxCourseIDQuery, null);
        
        if (cursor != null)
            cursor.moveToFirst();
        
        int result = cursor.getInt(0);
        Log.d("MaxReferenceKey: ", "Result: " + result);
        
        cursor.close();
        
		return result;
	}
	
	// Source for import/export technique found here:
	// http://stackoverflow.com/questions/6540906/android-simple-export-import-of-sqlite-database
	
	// Check if the database backup exists.
	
	public boolean doesBackupExist() {
		File sdCard = Environment.getExternalStorageDirectory();
		
		// Create file of database to be imported.
		File importedDatabase = new File(sdCard.getAbsolutePath() + "/Iuvo/IuvoDatabase.db");
		
		// Check if the database to be imported exists in file system.
		if(importedDatabase.exists())
			return true;
		else
			return false;
	}
	
	// Imports the database stored from the backup location and overwrites the app's database.
	public boolean importDatabase() throws IOException {
		
		// Creates the file directory at (SD card)/Iuvo if it doesn't exist.
		File sdCard = Environment.getExternalStorageDirectory();
		File fileDir = new File(sdCard.getAbsolutePath() + "/Iuvo");
		fileDir.mkdirs();
		
		// Create file of database to be imported.
		File importedDatabase = new File(fileDir.getAbsolutePath() + "/IuvoDatabase.db");
		File appDatabase = new File(DB_FILEPATH);
		
		// Check if the database to be imported exists in file system. Exit if it doesn't.
		if(!importedDatabase.exists())
			return false;
		
	    // Overwrite the app's database.
	    FileCopier.copyFile(new FileInputStream(importedDatabase), new FileOutputStream(appDatabase));
	    
	    return true;
	}
	
	// Exports the app's database
	public boolean exportDatabase() throws IOException {
		
		// Creates the file directory at (SD card)/Iuvo if it doens't exist.
		File sdCard = Environment.getExternalStorageDirectory();
		File fileDir = new File(sdCard.getAbsolutePath() + "/Iuvo");
		fileDir.mkdirs();
		
		// Create file of the data to be exported
		File exportedDatabase = new File(fileDir.getAbsolutePath() + "/IuvoDatabase.db");
		File appDatabase = new File(DB_FILEPATH);
		
		// If file exists already, delete it. We already confirmed with the user that we can overwrite.
		if(exportedDatabase.exists())
			exportedDatabase.delete();
		
		// Create file
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(exportedDatabase));
		} catch (FileNotFoundException e) {
			// File can't be written
			return false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					//Failed to close file.
					return false;
				}
			}
		}
		
		// Write the app's database to the exported file.
	    FileCopier.copyFile(new FileInputStream(appDatabase), new FileOutputStream(exportedDatabase));
	    
	    return true;
	}
	
	public String getGPA() {
		int hours = 0;
		double points = 0.00;
		int courseHours = 0;
		List<Course> courses = getAllCoursesNotExcludedFromGPA();
		
		if (courses.size() == 0) {
			return "0.0";
		}
		
		for(Course c : courses) {
			courseHours = c.getHours();
			
			// Can't use switch on strings. Shame.
			if (c.getGrade().equals("A ")) {
				points += 4.00 * courseHours;
				hours += courseHours;
			} else if (c.getGrade().equals("A-")) {
				points += 3.67 * courseHours;
				hours += courseHours;
			} else if (c.getGrade().equals("B+")) {
				points += 3.33 * courseHours;
				hours += courseHours;
			} else if (c.getGrade().equals("B ")) {
				points += 3.00 * courseHours;
				hours += courseHours;
			} else if (c.getGrade().equals("B-")) {
				points += 2.67 * courseHours;
				hours += courseHours;
			} else if (c.getGrade().equals("C+")) {
				points += 2.33 * courseHours;
				hours += courseHours;
			} else if (c.getGrade().equals("C ")) {
				points += 2.00 * courseHours;
				hours += courseHours;
			} else if (c.getGrade().equals("C-")) {
				points += 1.67 * courseHours;
				hours += courseHours;
			} else if (c.getGrade().equals("D+")) {
				points += 1.33 * courseHours;
				hours += courseHours;
			} else if (c.getGrade().equals("D ")) {
				points += 1.00 * courseHours;
				hours += courseHours;
			} else if (c.getGrade().equals("D-")) {
				points += 0.67 * courseHours;
				hours += courseHours;
			} else if (c.getGrade().equals("F ")) {
				hours += courseHours;
			}
		}
		
		if (hours == 0)
			return "0.0";
		
		double finalGPA = points / (double) hours;
		DecimalFormat format = new DecimalFormat("#.##");
		String finalGPAString = format.format(finalGPA);
		
		if (finalGPAString.equals("1") ||
				finalGPAString.equals("2") ||
				finalGPAString.equals("3") ||
				finalGPAString.equals("4"))
			finalGPAString += ".0";
		
		return finalGPAString;
	}
	
	// Get number of A's in degree plan
	public int getACount() {
		int count = 0;
		Cursor cursor;
		
	    String countQuery = "SELECT * FROM " + TABLE_COURSE + " WHERE " + 
	    		"(" + KEY_GROUP_ID + "!=? AND " + KEY_GRADE + "=?) OR " +
	    		"(" + KEY_GROUP_ID + "!=? AND " + KEY_GRADE + "=?)";
	    
	    
	    cursor = db.rawQuery(countQuery, new String[] {"-1", "A ", "-1", "A-"});
	    
	    count += cursor.getCount();
	    cursor.close();
	    
	    return count;
	}
	
	// Get number of B's in degree plan
	public int getBCount() {
		int count = 0;
		Cursor cursor;
		
	    String countQuery = "SELECT * FROM " + TABLE_COURSE + " WHERE " + 
	    		"(" + KEY_GROUP_ID + "!=? AND " + KEY_GRADE + "=?) OR " +
	    		"(" + KEY_GROUP_ID + "!=? AND " + KEY_GRADE + "=?) OR " +
	    		"(" + KEY_GROUP_ID + "!=? AND " + KEY_GRADE + "=?)";
	    
	    
	    cursor = db.rawQuery(countQuery, new String[] {"-1", "B+", "-1", "B ", "-1", "B-"});
	    
	    count += cursor.getCount();
	    cursor.close();
	    
	    return count;
	}
	
	// Get number of C's in degree plan
	public int getCCount() {
		int count = 0;
		Cursor cursor;
		
	    String countQuery = "SELECT * FROM " + TABLE_COURSE + " WHERE " + 
	    		"(" + KEY_GROUP_ID + "!=? AND " + KEY_GRADE + "=?) OR " +
	    		"(" + KEY_GROUP_ID + "!=? AND " + KEY_GRADE + "=?) OR " +
	    		"(" + KEY_GROUP_ID + "!=? AND " + KEY_GRADE + "=?)";
	    
	    
	    cursor = db.rawQuery(countQuery, new String[] {"-1", "C+", "-1", "C ", "-1", "C-"});
	    
	    count += cursor.getCount();
	    cursor.close();
	    
	    return count;
	}
	
	// Get number of D's in degree plan
	public int getDCount() {
		int count = 0;
		Cursor cursor;
		
	    String countQuery = "SELECT * FROM " + TABLE_COURSE + " WHERE " + 
	    		"(" + KEY_GROUP_ID + "!=? AND " + KEY_GRADE + "=?) OR " +
	    		"(" + KEY_GROUP_ID + "!=? AND " + KEY_GRADE + "=?) OR " +
	    		"(" + KEY_GROUP_ID + "!=? AND " + KEY_GRADE + "=?)";
	    
	    
	    cursor = db.rawQuery(countQuery, new String[] {"-1", "D+", "-1", "D ", "-1", "D-"});
	    
	    count += cursor.getCount();
	    cursor.close();
	    
	    return count;
	}
	
	// Get number of F's in degree plan
	public int getFCount() {
		int count = 0;
		Cursor cursor;
		
	    String countQuery = "SELECT * FROM " + TABLE_COURSE + " WHERE " + 
	    		"(" + KEY_GROUP_ID + "!=? AND " + KEY_GRADE + "=?)";
	    
	    
	    cursor = db.rawQuery(countQuery, new String[] {"-1", "F "});
	    
	    count += cursor.getCount();
	    cursor.close();
	    
	    return count;
	}
	
	// Get completed number of hours in degree plan
	public int getCompletedHours() {
		int hours = 0;
		Cursor cursor;
		
	    String countQuery = "SELECT " + KEY_HOURS + " FROM " + TABLE_COURSE + " WHERE " + 
	    		KEY_GRADE + "!=? AND " + KEY_GRADE + "!=? AND " + KEY_GROUP_ID + "!=?";
	    
	    
	    cursor = db.rawQuery(countQuery, new String[] {"None", "F ", "-1"});
	    
	    // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	hours += Integer.parseInt(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        
        cursor.close();
	    
	    return hours;
	}
	
	// Get total number of hours in degree plan
	public int getTotalHours() {
		int hours = 0;
		Cursor cursor;
		
	    String countQuery = "SELECT " + KEY_HOURS + " FROM " + TABLE_COURSE + " WHERE " + 
	    		 KEY_GROUP_ID + "!=?";
	    
	    
	    cursor = db.rawQuery(countQuery, new String[] {"-1"});
	    
	    // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	hours += Integer.parseInt(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        
        cursor.close();
	    
	    return hours;
	}
}
