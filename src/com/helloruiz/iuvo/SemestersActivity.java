package com.helloruiz.iuvo;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
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
	    
		public View getView(int position, View convertView, ViewGroup parent) {
	        View v = super.getView(position, convertView, parent);
	        
	        String courseCount = String.valueOf(databaseHandler.getCourseCountBySemesterPosition(position));
	        
	        if (courseCount.equals("1"))
	        	courseCount = courseCount + SINGLE_COURSE;
	        else
	        	courseCount = courseCount + MULTIPLE_COURSES;

	        TextView textView = (TextView) v.findViewById(R.id.semester_name_textview);
            textView.setTypeface(typeface);
            
            textView = (TextView) v.findViewById(R.id.semester_class_count_textview);
            textView.setText((CharSequence) courseCount);
            
            // Set list item to color of semester
            Semester semester = databaseHandler.getSemesterByPosition(position);
	        v.setBackgroundColor(ColorHandler.getColor(getContext(), semester.getColor()));
	        
	        return v;
	      }
	    }
	
	/**
	 * -- Variables --
	 */
	private DatabaseHandler databaseHandler;
	
	private SemesterAdapter semesterAdapter;

    private ArrayList<Semester> mSemesters;
    
    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
                
    	@Override
    	public void drop(int from, int to) {
    		
			databaseHandler.moveSemester(from, to);      
    		
			refreshListAdapter();
			getListView().setSelection(to - 2);
    	}
    };
	
	// Whenever a user swipes an semester item left or right to delete.
    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
		
		@Override
		public void remove(int which) {
			deleteSemesterConfirm(which);			
		}
	};
	
	// Typeface for pretty lobster font.
	Typeface typeface;

	// Strings defined globally here since they'll be used in a loop.
	String SINGLE_COURSE = " Course";
	String MULTIPLE_COURSES = " Courses";
	
	// Unique tags for passing an intent to another activity.
    static String SEMESTERSACTIVITY_SEMESTER_ID = "com.helloruiz.iuvo.SemestersActivity.semesterID";
		
	/**
	 * -- Overrides --
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semesters);
		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/lobster.otf");
		databaseHandler = new DatabaseHandler(this);
		
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
		v.setTag(item.getID());
		semesterOptionsDialog(v);
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
	public void semesterOptionsDialog(final View view) {
		DatabaseHandler db = new DatabaseHandler(this);
		Semester semester = db.getSemester((Integer) view.getTag());
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(semester.getName())
	           .setItems(R.array.semester_option_array, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   Intent intent;
	            	   int semesterID = (Integer) view.getTag();
		            	if (which == 0) {
		            	   intent = new Intent(getApplicationContext(), CoursesActivitySemester.class);
		            	   intent.putExtra(SEMESTERSACTIVITY_SEMESTER_ID, semesterID);
		            	   startActivity(intent); 
		                } else {
		            	   intent = new Intent(getApplicationContext(), SemesterActivity.class);
		            	   intent.putExtra(SEMESTERSACTIVITY_SEMESTER_ID, semesterID);
		            	   startActivity(intent); 
		               }
	           }
	    });
	    
	    builder.show();
	}
	
	// Pops up with dialog so that user can add a semester
    public void menuSemestersHelp() {
    	Intent intent = new Intent(getApplicationContext(), SemestersHelpActivity.class);
    	startActivity(intent);
    }
	
	// Starts add semester activity
    public void menuAddSemester() {
    	Intent intent = new Intent(this, SemesterActivity.class);
    	startActivity(intent);
    }
    
    // Pops up with dialog so that user can confirm deletion
    public void deleteSemesterConfirm(int which) {
    	Dialogs.deleteSemesterConfirm(this, which);
    }

    // Executes after user adds a semester from addMenuSemester
	public void addSemester(String name, String color) {
		databaseHandler.addSemester(name, color);

		refreshListAdapter();
	}
	
	// Called after a user has confirmed that they want to edit a semester
	public void editSemester(String newName, String newColor, Semester item) {
		
		item.setName(newName);
		item.setColor(newColor);
		databaseHandler.updateSemester(item);
		
		refreshListAdapter();
	}
	
	// Executes after user has confirmed that they want to delete a semester
	public void deleteSemester(int which) {

		Semester item = databaseHandler.getSemesterByPosition(which);
		
		// Remove semester from database
		databaseHandler.deleteSemester(item);
					
		refreshListAdapter();
	}	
	
	// Called whenever the Drag Sort ListView needs to be updated to reflect database changes
	public void refreshListAdapter() {
		
		// Refresh the ListAdapter to reflect the new changes in the database.
		List<Semester> semestersInDatabase = databaseHandler.getAllSemesters();
		
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
