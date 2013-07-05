package com.helloruiz.iuvo.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database Handler for all group, semester, and class databases. Based on:
 * http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
 * 
 * Handler operations assume that the all of the IDs for either group or semester sequential from 0 with no gaps.
 * ex. 0, 1, 2 or 0 1, 2, 3, 4, 5, 6
 */
public class DatabaseHandler extends SQLiteOpenHelper {

	/**
	 * -- Static Variables --
	 */
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "IuvoDatabase";
 
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

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    /**
	 * -- Overrides --
	 */
    // Creating Tables
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
        
        ;
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
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_GROUP, null, values);
        ;
    }
    
    // Move group position in list.
    public void moveGroup(int from, int to) {
    	SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getReadableDatabase();
     
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
        SQLiteDatabase db = this.getReadableDatabase();
     
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
     
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
     
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
    	
        SQLiteDatabase db = this.getWritableDatabase();
        
        // Delete all courses associated with this group
        db.delete(TABLE_COURSE, KEY_GROUP_ID + " = ?", new String[] { String.valueOf(group.getID()) });
        
        // Delete the group
        db.delete(TABLE_GROUP, KEY_ID + " = ?",new String[] { String.valueOf(group.getID()) });
        
        // Decrement all of the groups below the group that was deleted
        for (int i = group.getPosition() + 1; i <= groupCount; i++) {
			ContentValues values = new ContentValues();
			values.put(KEY_POSITION, i - 1);
			db.update(TABLE_GROUP, values, KEY_POSITION + "=?", new String[] {String.valueOf(i)});
		}
    }

	// Get group count
	public int getGroupCount() {
		SQLiteDatabase db = this.getWritableDatabase();
		
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
        
        SQLiteDatabase db = this.getWritableDatabase();
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
		SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_SEMESTER, null, values);
        ;
    }
    
    // Move semester position in list.
    public void moveSemester(int from, int to) {
    	SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getReadableDatabase();
     
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
        SQLiteDatabase db = this.getReadableDatabase();
     
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
     
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
	 
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
    	
        SQLiteDatabase db = this.getWritableDatabase();
        
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
		SQLiteDatabase db = this.getWritableDatabase();
		
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
        
        SQLiteDatabase db = this.getWritableDatabase();
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
		SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_COURSE, null, values);
        ;
    }
    
    // Move course position in list. Position applies to group that the course is in.
    public void moveCourse(int from, int to, int groupID) {
    	SQLiteDatabase db = this.getWritableDatabase();
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
    
    // Get single course
    public Course getCourse(int position) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = db.query(TABLE_COURSE, 
        		new String[] {KEY_ID, KEY_POSITION, KEY_NAME, KEY_HOURS, KEY_GRADE, KEY_EXCLUDED_FROM_GPA, KEY_SEMESTER_ID, KEY_GROUP_ID},
        		KEY_ID + "=?",
        		new String[] { String.valueOf( position )},
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
        SQLiteDatabase db = this.getReadableDatabase();
     
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
     
        SQLiteDatabase db = this.getWritableDatabase();
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
     
        SQLiteDatabase db = this.getWritableDatabase();
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
        String selectQuery = "SELECT * FROM " + TABLE_COURSE + " WHERE " + KEY_SEMESTER_ID + "=" + String.valueOf(semesterID) + " ORDER BY " + KEY_POSITION;
     
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
	 
        ContentValues values = new ContentValues();
        
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
    	
        SQLiteDatabase db = this.getWritableDatabase();
        
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
		SQLiteDatabase db = this.getWritableDatabase();
		
	    String countQuery = "SELECT * FROM " + TABLE_COURSE;
	    Cursor cursor = db.rawQuery(countQuery, null);
	    
	    int count = cursor.getCount();
	    cursor.close();
	    
	    // return count
	    return count;
	}
	
	// Get course count by group
	public int getCourseCountByGroup(int groupID) {
		SQLiteDatabase db = this.getWritableDatabase();
		
	    String countQuery = "SELECT * FROM " + TABLE_COURSE + " WHERE " + KEY_GROUP_ID + "=" + String.valueOf(groupID);
	    Cursor cursor = db.rawQuery(countQuery, null);
	    
	    int count = cursor.getCount();
	    cursor.close();
	    
	    // return count
	    return count;
	}
	
	// Get course count by semester
	public int getCourseCountBySemester(int semesterID) {
		SQLiteDatabase db = this.getWritableDatabase();
		
	    String countQuery = "SELECT * FROM " + TABLE_COURSE + " WHERE " + KEY_SEMESTER_ID + "=" + String.valueOf(semesterID);
	    Cursor cursor = db.rawQuery(countQuery, null);
	    
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
        
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(maxCourseIDQuery, null);
        
        if (cursor != null)
            cursor.moveToFirst();
        
        int result = cursor.getInt(0);
        Log.d("MaxReferenceKey: ", "Result: " + result);
        
        cursor.close();
        
		return result;
	}
}
