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
 * http://www.androidhive.info/2011/11/android-sqlite-com.helloruiz.iuvo.database-tutorial/
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "IuvoDatabase";
 
    // Group table name
    private static final String TABLE_GROUP = "groups";
 
    // Group Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GROUP_TABLE = "create table " + TABLE_GROUP + "("
        		+ KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_GROUP_TABLE);
    }
 
    // Upgrading com.helloruiz.iuvo.database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
 
        // Create tables again
        onCreate(db);
    }
    
    // Add new group
    public void addGroup(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_ID, group.getID());
        values.put(KEY_NAME, group.getName()); // Group Name
     
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
     
        // return group list
        return groupList;
    }
    
    // Get group count
    public int getGroupCount() {
        String countQuery = "SELECT * FROM " + TABLE_GROUP;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        
        int count = cursor.getCount();
        cursor.close();
        
        // return count
        return count;
    }
    
    // Update single group name.
    public int updateGroupName(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_ID, group.getID());
        values.put(KEY_NAME, group.getName());
     
        // updating row
        return db.update(TABLE_GROUP, values, KEY_ID + " = ?",
                new String[] { String.valueOf(group.getID()) });
    }
    
    // Increment or decrement group ID.
    public int updateGroupID(Group group, boolean incrementID) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        if (incrementID)
        	values.put(KEY_ID, group.getID() + 1);
        else
        	values.put(KEY_ID, group.getID() - 1);
        values.put(KEY_NAME, group.getName());
     
        // updating row
        return db.update(TABLE_GROUP, values, KEY_ID + " = ?",
                new String[] { String.valueOf(group.getID()) });
    }
    
    // Delete single group 
    public void deleteGroup(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();
        int max = getGroupCount();
        
        db.delete(TABLE_GROUP, KEY_ID + " = ?",
                new String[] { String.valueOf(group.getID()) });
        
        // Decrements all of the groups below the group to delete.
        for (int i = group.getID() + 1; i < max; i++) {
        	updateGroupID(getGroup(i), false);
        }
        db.close();
    }
    
    
}
