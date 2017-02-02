package com.helloruiz.iuvo;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.helloruiz.iuvo.database.IuvoApplication;
import com.helloruiz.iuvo.database.Semester;
import com.helloruiz.iuvo.help.SemestersHelpActivity;
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

		@NonNull
		public View getView(int position, View convertView, @NonNull ViewGroup parent) {
	        View v = super.getView(position, convertView, parent);
	        
	        String courseCount = String.valueOf(IuvoApplication.db.getCourseCountBySemesterPosition(position));
	        
	        if (courseCount.equals("1"))
	        	courseCount = courseCount + SINGLE_COURSE;
	        else
	        	courseCount = courseCount + MULTIPLE_COURSES;
	        
	        String semesterGPA = String.valueOf(IuvoApplication.db.getGPABySemesterPosition(position));
	        semesterGPA = ", " + semesterGPA + " GPA";

	        TextView textView = (TextView) v.findViewById(R.id.semester_name_textview);
            textView.setTypeface(IuvoApplication.typeface);
            
            textView = (TextView) v.findViewById(R.id.semester_class_count_textview);
            textView.setText( (courseCount + semesterGPA));
            
            // Set list item to color of semester
            Semester semester = IuvoApplication.db.getSemesterByPosition(position);
	        v.setBackgroundColor(ColorHandler.getColor(getContext(), semester.getColor()));
	        
	        return v;
	      }
	    }
	
	/**
	 * -- Variables --
	 */
	
	private SemesterAdapter semesterAdapter;

    private ArrayList<Semester> mSemesters;
    
    private final DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
                
    	@Override
    	public void drop(int from, int to) {
    		
			IuvoApplication.db.moveSemester(from, to);      
    		
			refreshListAdapter();
			getListView().setSelection(to - 2);
    	}
    };
	
	// Whenever a user swipes an semester item left or right to delete.
    private final DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
		
		@Override
		public void remove(int which) {
			deleteSemesterConfirm(which);			
		}
	};

	// Strings defined globally here since they'll be used in a loop.
	final String SINGLE_COURSE = " Course";
	final String MULTIPLE_COURSES = " Courses";
	
	// Unique tags for passing an intent to another activity.
    static final String SEMESTERS_ACTIVITY_SEMESTER_ID = "com.helloruiz.iuvo.SemestersActivity.semesterID";
		
	/**
	 * -- Overrides --
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semesters);
		
		// Show the Up button in the action bar.
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);

		}
		
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
	public void onResume() {
		super.onResume();
		refreshListAdapter();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Semester item = semesterAdapter.getItem(position);
		if (item != null) {
			v.setTag(item.getID());
			semesterOptionsDialog(v);
		}

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
	private void semesterOptionsDialog(final View view) {
		Semester semester = IuvoApplication.db.getSemester((Integer) view.getTag());
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(semester.getName())
	           .setItems(R.array.semester_option_array, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   Intent intent;
	            	   int semesterID = (Integer) view.getTag();
		            	if (which == 0) {
		            	   intent = new Intent(getApplicationContext(), CoursesActivitySemester.class);
		            	   intent.putExtra(SEMESTERS_ACTIVITY_SEMESTER_ID, semesterID);
		            	   startActivity(intent); 
		                } else {
		            	   intent = new Intent(getApplicationContext(), SemesterActivity.class);
		            	   intent.putExtra(SEMESTERS_ACTIVITY_SEMESTER_ID, semesterID);
		            	   startActivity(intent); 
		               }
	           }
	    });
	    
	    builder.show();
	}
	
	// Pops up with dialog so that user can add a semester
	private void menuSemestersHelp() {
    	Intent intent = new Intent(getApplicationContext(), SemestersHelpActivity.class);
    	startActivity(intent);
    }
	
	// Starts add semester activity
	private void menuAddSemester() {
    	Intent intent = new Intent(this, SemesterActivity.class);
    	startActivity(intent);
    }
    
    // Pops up with dialog so that user can confirm deletion
	private void deleteSemesterConfirm(int which) {
    	Dialogs.deleteSemesterConfirm(this, which);
    }

    // Executes after user adds a semester from addMenuSemester
	public void addSemester(String name, String color) {
		IuvoApplication.db.addSemester(name, color);

		refreshListAdapter();
	}
	
	// Called after a user has confirmed that they want to edit a semester
	public void editSemester(String newName, String newColor, Semester item) {
		
		item.setName(newName);
		item.setColor(newColor);
		IuvoApplication.db.updateSemester(item);
		
		refreshListAdapter();
	}
	
	// Executes after user has confirmed that they want to delete a semester
	public void deleteSemester(int which) {

		Semester item = IuvoApplication.db.getSemesterByPosition(which);
		
		// Remove semester from database
		IuvoApplication.db.deleteSemester(item);
					
		refreshListAdapter();
	}	
	
	// Called whenever the Drag Sort ListView needs to be updated to reflect database changes
	public void refreshListAdapter() {
		
		// Refresh the ListAdapter to reflect the new changes in the database.
		List<Semester> semestersInDatabase = IuvoApplication.db.getAllSemesters();
		
		Log.d("Semester: ", "Updating ListAdapter...");
		mSemesters = new ArrayList<Semester>();
		for (Semester g : semestersInDatabase) {
			Log.d("Semester: ", "Position: " + g.getPosition() + ", ID: " + g.getID() + ", Name: " + g.getName() + ", Color: " + g.getColor());
			mSemesters.add(g);
		}
		
		semesterAdapter = new SemesterAdapter(mSemesters);
		setListAdapter(semesterAdapter);
	}
}
