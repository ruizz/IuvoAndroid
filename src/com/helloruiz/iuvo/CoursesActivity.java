package com.helloruiz.iuvo;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
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
import com.helloruiz.iuvo.database.Course;
import com.helloruiz.iuvo.database.Semester;
import com.mobeta.android.dslv.DragSortListView;

public class CoursesActivity extends ListActivity {
	
	/**
	 * -- Classes --
	 */
	private class CourseAdapter extends ArrayAdapter<Course> {
	      
	    public CourseAdapter(List<Course> courses) {
	    	super(CoursesActivity.this, R.layout.activity_courses_list_item,
	    			R.id.course_name_textview, courses);
	    }
	    
		public View getView(int position, View convertView, ViewGroup parent) {
	        View v = super.getView(position, convertView, parent);
	        
	        DatabaseHandler db = new DatabaseHandler(CoursesActivity.this);
	        Course course = db.getCourseByPosition(position, groupID);
	        
	        TextView textView = (TextView) v.findViewById(R.id.course_name_textview);
	        Typeface typeFace=Typeface.createFromAsset(v.getContext().getAssets(),"fonts/lobster.otf");
            textView.setTypeface(typeFace);
	        textView.setText((CharSequence) course.getName() + "(" + String.valueOf(course.getHours()) + ")");
	        
	        textView = (TextView) v.findViewById(R.id.course_semester_textview);
            
            if (course.getSemesterID() != -1) {
            	Semester semester = db.getSemester(course.getSemesterID());
            	textView.setText(semester.getName());
            	v.setBackgroundColor(ColorHandler.getColor(getContext(), semester.getColor()));
            } else
            	v.setBackgroundColor(ColorHandler.getColor(getContext(), "gray"));
	        return v;
	      }
	    }
	
	/**
	 * -- Variables --
	 */
	int groupID = -1;
	
	private CourseAdapter courseAdapter;

    private ArrayList<Course> mCourses;
    
    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
                
    	@Override
    	public void drop(int from, int to) {
    		
			DatabaseHandler databaseHandler = new DatabaseHandler(CoursesActivity.this);
			databaseHandler.moveCourse(from, to, groupID);      
    		
			refreshListAdapter();
			getListView().setSelection(to);
    	}
    };
	
	// Whenever a user swipes an course item left or right to delete.
    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
		
		@Override
		public void remove(int which) {
			deleteCourseConfirm(which);			
		}
	};

	/**
	 * -- Overrides --
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courses);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		groupID = intent.getIntExtra(MainActivity.MAINACTIVITY_GROUP_ID, -1);
		
		DatabaseHandler db = new DatabaseHandler(this);
		if (groupID != -1)
			setTitle(db.getGroup(groupID).getName());
		
		// Set up our drag sort ListView
		DragSortListView dragSortListView = (DragSortListView) getListView();
		dragSortListView.setDropListener(onDrop);
		dragSortListView.setRemoveListener(onRemove);
		
		refreshListAdapter();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.courses, menu);
		return true;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Course is editable from degree plan view. No need here.
		// Course item = courseAdapter.getItem(position);
		// Dialogs.editCourse(this, item);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		//case R.id.menu_course_help:
			//menuCoursesHelp();
			//return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * -- Voids --
	 */
	// Pops up with dialog so that user can add a course
    public void menuCoursesHelp() {
    	// Dialogs.coursesHelp(this);
    }
    
    // Pops up with dialog so that user can confirm deletion
    public void deleteCourseConfirm(int which) {
    	Dialogs.deleteCourseConfirm(this, which);
    }
	
	// Executes after user has confirmed that they want to delete a course
	public void deleteCourse(int which) {
		DatabaseHandler databaseHandler = new DatabaseHandler(this);

		Course item = databaseHandler.getCourseByPosition(which, groupID);
		
		// Remove course from database
		databaseHandler.deleteCourse(item);
					
		refreshListAdapter();
	}	
	
	// Called whenever the Drag Sort ListView needs to be updated to reflect database changes
	public void refreshListAdapter() {
		// Refresh the ListAdapter to reflect the new changes in the database.
		DatabaseHandler databaseHandler = new DatabaseHandler(this);
		List<Course> coursesInDatabase = databaseHandler.getAllCourses();
		
		Log.d("Course: ", "Updating ListAdapter...");
		mCourses = new ArrayList<Course>();
		for (Course c : coursesInDatabase) {
			Log.d("All Courses Group", "Position: " + c.getPosition() + ", ID: " + c.getID() + ", gID: "+ c.getGroupID() + ", sID: " + c.getSemesterID() + ", Name: " + c.getName());
			mCourses.add(c);
		}
		
		courseAdapter = new CourseAdapter(mCourses);
		setListAdapter(courseAdapter);
	}
}
