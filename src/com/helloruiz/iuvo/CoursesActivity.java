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

import com.helloruiz.iuvo.database.Course;
import com.helloruiz.iuvo.database.IuvoApplication;
import com.helloruiz.iuvo.database.Semester;
import com.helloruiz.iuvo.help.CoursesHelpActivity;
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
	        
	        Course course = mCourses.get(position);
	        
	        TextView textView = (TextView) v.findViewById(R.id.course_name_textview);
            //textView.setTypeface(typeface);
	        textView.setText((CharSequence) course.getName() + " (" + String.valueOf(course.getHours()) + ")");
	        
	        textView = (TextView) v.findViewById(R.id.course_semester_textview);
            
            if (course.getSemesterID() != -1) {
            	Semester semester = IuvoApplication.db.getSemester(course.getSemesterID());
            	textView.setText(semester.getName());
            	v.setBackgroundColor(ColorHandler.getColor(getContext(), semester.getColor()));
            } else
            	v.setBackgroundColor(ColorHandler.getColor(getContext(), "gray"));
            
            textView = (TextView) v.findViewById(R.id.course_grade_textview);
            textView.setTypeface(typeface);
            if (course.getGrade().equals("None"))
            	textView.setText("");
            else
            	textView.setText((CharSequence) course.getGrade());
            
	        return v;
		}
	}
	
	/**
	 * -- Variables --
	 */

	// For determining what group we're loading a list of courses from.
	int groupID = -1;
	
	private CourseAdapter courseAdapter;

    private ArrayList<Course> mCourses;
    
    // Listener for whenever a user drops a course in a new location
    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
    	@Override
    	public void drop(int from, int to) {
			IuvoApplication.db.moveCourse(from, to, groupID);      
			refreshListAdapter();
			getListView().setSelection(to - 2);
    	}
    };
	
	// Listener for whenever a user swipes an course item left or right to delete.
    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
		@Override
		public void remove(int which) {
			deleteCourseConfirm(which);			
		}
	};
	
	// Typeface for pretty lobster front.
	Typeface typeface;

	/**
	 * -- Overrides --
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/lobster.otf");
		
		setContentView(R.layout.activity_courses);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		groupID = intent.getIntExtra(MainActivity.MAINACTIVITY_GROUP_ID, -1);
		
		if (groupID != -1)
			setTitle(IuvoApplication.db.getGroup(groupID).getName());
		
		// Set up our drag sort ListView
		DragSortListView dragSortListView = (DragSortListView) getListView();
		dragSortListView.setDropListener(onDrop);
		dragSortListView.setRemoveListener(onRemove);
		
		refreshListAdapter();
	}
	
	@Override
	public void onResume() {
		super.onResume();
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
		Intent intent = new Intent(getApplicationContext(), CourseActivity.class);
		intent.putExtra(MainActivity.MAINACTIVITY_COURSE_ID, mCourses.get(position).getID());
		startActivity(intent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.menu_courses_help:
			menuCoursesHelp();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * -- Voids --
	 */
	// Pops up with dialog so that user get help
    public void menuCoursesHelp() {
    	Intent intent = new Intent(getApplicationContext(), CoursesHelpActivity.class);
    	startActivity(intent);
    }
    
    // Pops up with dialog so that user can confirm deletion
    public void deleteCourseConfirm(int which) {
    	Dialogs.deleteCourseConfirm(this, which);
    }
	
	// Executes after user has confirmed that they want to delete a course
	public void deleteCourse(int which) {

		Course item = IuvoApplication.db.getCourseByPosition(which, groupID);
		
		// Remove course from database
		IuvoApplication.db.deleteCourse(item);
					
		refreshListAdapter();
	}	
	
	// Called whenever the Drag Sort ListView needs to be updated to reflect database changes
	public void refreshListAdapter() {
		// Refresh the ListAdapter to reflect the new changes in the database.
		List<Course> coursesInDatabase = IuvoApplication.db.getAllCoursesByGroup(groupID);
		
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
