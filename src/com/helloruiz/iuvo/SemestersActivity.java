package com.helloruiz.iuvo;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.helloruiz.iuvo.database.DatabaseHandler;
import com.helloruiz.iuvo.database.Semester;
import com.mobeta.android.dslv.DragSortListView;

public class SemestersActivity extends ListActivity {
	
	/**
	 * -- Classes --
	 */
	private class SemesterAdapter extends ArrayAdapter<Semester> {
	      
	      public SemesterAdapter(List<Semester> semesters) {
	        super(SemestersActivity.this, R.layout.activity_semesters_list_item,
	          R.id.semester_name_textview, semesters);
	      }

		public View getView(int position, View convertView, ViewGroup parent) {
	        View v = super.getView(position, convertView, parent);
	        
	        DatabaseHandler databaseHandler = new DatabaseHandler(SemestersActivity.this);
	        Semester semester = databaseHandler.getSemester(position);
	        
	        TextView textView = (TextView) v.findViewById(R.id.semester_name_textview);
	        Typeface typeFace=Typeface.createFromAsset(v.getContext().getAssets(),"fonts/lobster.otf");
            textView.setTypeface(typeFace);
	        
	        v.setBackgroundColor(ColorHandler.getColor(SemestersActivity.this, semester.getColor()));
	        return v;
	      }
	    }
	
	/**
	 * -- Static Variables --
	 */
	private SemesterAdapter semesterAdapter;

    private ArrayList<Semester> mSemesters;
    
    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
                
    	@Override
    	public void drop(int from, int to) {
    		if (from != to) {
    		Semester item = semesterAdapter.getItem(from);
    		
			// Remove semester from database
			DatabaseHandler databaseHandler = new DatabaseHandler(SemestersActivity.this);
			databaseHandler.deleteSemester(item);
			
			// Change ID to new position before inserting into database
			item.setID(to);
			databaseHandler.insertSemester(item);      
    		
			refreshListAdapter();
			getListView().setSelection(to);
    		}
    	}
    };
	
	// Whenever a user swipes an semester item left or right to delete.
    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
		
		@Override
		public void remove(int which) {
			deleteSemesterConfirm(which);			
		}
	};

	/**
	 * -- Overrides --
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semesters);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Set up our drag sort ListView
		DragSortListView dragSortListView = (DragSortListView) getListView();
		dragSortListView.setDropListener(onDrop);
		dragSortListView.setRemoveListener(onRemove);
		
		refreshListAdapter();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.semesters, menu);
		return true;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Semester item = semesterAdapter.getItem(position);
		Dialogs.editSemester(this, item);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.menu_semester_help:
			menuSemestersHelp();
			return true;
		case R.id.menu_semester_add:
			menuAddSemester();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * -- Voids --
	 */
	// Pops up with dialog so that user can add a semester
    public void menuSemestersHelp() {
    	Dialogs.semestersHelp(this);
    }
	
	// Pops up with dialog so that user can add a semester
    public void menuAddSemester() {
    	Dialogs.addSemester(this);
    }
    
    // Pops up with dialog so that user can confirm deletion
    public void deleteSemesterConfirm(int which) {
    	Dialogs.deleteSemesterConfirm(this, which);
    }

    // Executes after user adds a semester from addMenuSemester
	public void addSemester(String name, String color) {
		DatabaseHandler databaseHandler = new DatabaseHandler(this);
		SQLiteDatabase db = databaseHandler.getWritableDatabase();
		
		// This should be correct, since new max id is current number of available semesters.
		int max = databaseHandler.getSemesterCount(db);
		int maxReferenceKey;
		
		if (max > 0)
			maxReferenceKey = databaseHandler.getMaxSemesterReferenceKey();
		else
			maxReferenceKey = -1;
		
		databaseHandler.addSemester(new Semester(max, maxReferenceKey + 1, name, color));

		refreshListAdapter();
		db.close();
	}
	
	// Called after a user has confirmed that they want to edit a semester
	public void editSemester(String newName, String newColor, Semester item) {
		DatabaseHandler databaseHandler = new DatabaseHandler(this);
		
		item.setName(newName);
		item.setColor(newColor);
		databaseHandler.updateSemester(item);
		
		refreshListAdapter();
	}
	
	// Executes after user has confirmed that they want to delete a semester
	public void deleteSemester(int which) {
		DatabaseHandler databaseHandler = new DatabaseHandler(this);

		Semester item = databaseHandler.getSemester(which);
		
		// Remove semester from database
		databaseHandler.deleteSemester(item);
					
		refreshListAdapter();
	}	
	
	// Called whenever the Drag Sort ListView needs to be updated to reflect database changes
	public void refreshListAdapter() {
		// Refresh the ListAdapter to reflect the new changes in the database.
		DatabaseHandler databaseHandler = new DatabaseHandler(SemestersActivity.this);
		List<Semester> semestersInDatabase = databaseHandler.getAllSemesters();
		
		Log.d("Semester: ", "Updating ListAdapter...");
		mSemesters = new ArrayList<Semester>();
		for (Semester g : semestersInDatabase) {
			Log.d("Semester: ", "ID: " + g.getID() + ", Name: " + g.getName() + ", Color: " + g.getColor() + ", ReferenceKey: " + g.getReferenceKey());
			mSemesters.add(g);
		}
		
		semesterAdapter = new SemesterAdapter(mSemesters);
		setListAdapter(semesterAdapter);
	}
}
