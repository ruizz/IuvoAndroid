package com.helloruiz.iuvo.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database Handler for all group, semester, and class databases. Based on:
 * http://www.androidhive.info/2011/11/android-sqlite-com.helloruiz.iuvo.database-tutorial/
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
 
    // Group Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_COLOR = "color";
 
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
                + KEY_NAME + " TEXT" + ")";
        
        // Table
        String CREATE_SEMESTER_TABLE = "create table " + TABLE_SEMESTER + "("
        		+ KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT," 
        		+ KEY_COLOR + " TEXT" + ")";
        
        db.execSQL(CREATE_GROUP_TABLE);
        db.execSQL(CREATE_SEMESTER_TABLE);
    }
 
    // I'll worry about this if I ever have to upgrade the database. Leave clear for now.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
    
    /**
	 * -- Group Operations --
	 */
    // Add new group
    public void addGroup(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_ID, group.getID());
        values.put(KEY_NAME, group.getName());
     
        // Adding Row
        db.insert(TABLE_GROUP, null, values);
        db.close();
    }
    
    // Insert group at a specified location.
    public void insertGroup(Group group) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
        values.put(KEY_ID, group.getID());
        values.put(KEY_NAME, group.getName());
		
        // Increments all of the groups below the group to insert.
        for (int i = getGroupCount(db) - 1; i > group.getID() - 1; i--) {
        	updateGroupID(db, getGroup(i), true);
        }
        
        // Inserting Row
        db.insert(TABLE_GROUP, null, values);
        db.close(); 
    }
    
    // Get single group
    public Group getGroup(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = db.query(TABLE_GROUP, new String[] {KEY_ID, KEY_NAME}, KEY_ID + "=?", new String[] { String.valueOf( id ) },
        		null, null, null, null);
        
        if (cursor != null)
            cursor.moveToFirst();
     
        Group group = new Group(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
        
        cursor.close();
        
        // return group
        return group;
    }
    
    // Get all groups
    public List<Group> getAllGroups() {
        List<Group> groupList = new ArrayList<Group>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GROUP;
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Group group = new Group(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
                
                // Adding group to list
                groupList.add(group);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
     
        // return group list
        return groupList;
    }
    
    // Update single group name
    public void updateGroupName(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_ID, group.getID());
        values.put(KEY_NAME, group.getName());
     
        // updating row
        db.update(TABLE_GROUP, values, KEY_ID + " = ?", new String[] { String.valueOf(group.getID()) });
        db.close();
    }
    
    // Increment or decrement group ID.
    public void updateGroupID(SQLiteDatabase db, Group group, boolean incrementID) {
     
        ContentValues values = new ContentValues();
        if (incrementID)
        	values.put(KEY_ID, group.getID() + 1);
        else
        	values.put(KEY_ID, group.getID() - 1);
        values.put(KEY_NAME, group.getName());
     
        // updating row
        db.update(TABLE_GROUP, values, KEY_ID + " = ?", new String[] { String.valueOf(group.getID()) });
    }
    
    // Delete single group 
    public void deleteGroup(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        // Group count before deletion
        int groupCount = getGroupCount(db);
        
        db.delete(TABLE_GROUP, KEY_ID + " = ?",
                new String[] { String.valueOf(group.getID()) });
        
        // Decrements all of the groups below the group to delete.
        for (int i = group.getID() + 1; i < groupCount; i++) {
        	updateGroupID(db, getGroup(i), false);
        }
        db.close();
    }

	// Get group count
	public int getGroupCount(SQLiteDatabase db) {
	    String countQuery = "SELECT * FROM " + TABLE_GROUP;
	    Cursor cursor = db.rawQuery(countQuery, null);
	    
	    int count = cursor.getCount();
	    cursor.close();
	    
	    // return count
	    return count;
	}
	
	/**
	 * -- Semester Operations --
	 */
    // Add new semester
    public void addSemester(Semester semester) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_ID, semester.getID());
        values.put(KEY_NAME, semester.getName());
		values.put(KEY_COLOR, semester.getColor());
     
        // Adding Row
        db.insert(TABLE_SEMESTER, null, values);
        db.close();
    }
    
    // Insert semester at a specified location.
    public void insertSemester(Semester semester) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
        values.put(KEY_ID, semester.getID());
        values.put(KEY_NAME, semester.getName());
        values.put(KEY_COLOR, semester.getColor());
		
        // Increments all of the semesters below the semester to insert.
        for (int i = getSemesterCount(db) - 1; i > semester.getID() - 1; i--) {
        	updateSemesterID(db, getSemester(i), true);
        }
        
        // Inserting Row
        db.insert(TABLE_SEMESTER, null, values);
        db.close(); 
    }
    
    // Get single semester
    public Semester getSemester(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = db.query(TABLE_SEMESTER, new String[] {KEY_ID, KEY_NAME, KEY_COLOR}, KEY_ID + "=?", new String[] { String.valueOf( id ) },
        		null, null, null, null);
        
        if (cursor != null)
            cursor.moveToFirst();
     
        Semester semester = new Semester(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
        
        cursor.close();
        
        // return semester
        return semester;
    }
    
    // Get all semesters
    public List<Semester> getAllSemesters() {
        List<Semester> semesterList = new ArrayList<Semester>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SEMESTER;
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Semester semester = new Semester(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
                
                // Adding semester to list
                semesterList.add(semester);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
     
        // return semester list
        return semesterList;
    }
    
    // Update single semester name and color.
    public void updateSemesterName(Semester semester) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_ID, semester.getID());
        values.put(KEY_NAME, semester.getName());
		values.put(KEY_COLOR, semester.getColor());
     
        // updating row
        db.update(TABLE_SEMESTER, values, KEY_ID + " = ?", new String[] { String.valueOf(semester.getID()) });
        db.close();
    }
    
    // Increment or decrement semester ID.
    public void updateSemesterID(SQLiteDatabase db, Semester semester, boolean incrementID) {
     
        ContentValues values = new ContentValues();
        if (incrementID)
        	values.put(KEY_ID, semester.getID() + 1);
        else
        	values.put(KEY_ID, semester.getID() - 1);
        values.put(KEY_NAME, semester.getName());
		values.put(KEY_COLOR, semester.getColor());
     
        // updating row
        db.update(TABLE_SEMESTER, values, KEY_ID + " = ?", new String[] { String.valueOf(semester.getID()) });
    }
    
    // Delete single semester 
    public void deleteSemester(Semester semester) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        // Semester count before deletion
        int semesterCount = getSemesterCount(db);
        
        db.delete(TABLE_SEMESTER, KEY_ID + " = ?",
                new String[] { String.valueOf(semester.getID()) });
        
        // Decrements all of the semesters below the semester to delete.
        for (int i = semester.getID() + 1; i < semesterCount; i++) {
        	updateSemesterID(db, getSemester(i), false);
        }
        db.close();
    }

	// Get semester count
	public int getSemesterCount(SQLiteDatabase db) {
	    String countQuery = "SELECT * FROM " + TABLE_SEMESTER;
	    Cursor cursor = db.rawQuery(countQuery, null);
	    
	    int count = cursor.getCount();
	    cursor.close();
	    
	    // return count
	    return count;
	}
}
