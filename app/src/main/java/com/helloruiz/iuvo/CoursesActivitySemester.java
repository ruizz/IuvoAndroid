package com.helloruiz.iuvo;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.helloruiz.iuvo.database.Course;
import com.helloruiz.iuvo.database.IuvoApplication;
import com.helloruiz.iuvo.database.Semester;
import com.helloruiz.iuvo.help.CoursesHelpActivity;
import com.mobeta.android.dslv.DragSortListView;

public class CoursesActivitySemester extends ListActivity {

	/**
	 * -- Classes --
	 */
	private class CoursesAdapter extends ArrayAdapter<Course> {
	      
	    public CoursesAdapter(List<Course> courses) {
	    	super(CoursesActivitySemester.this, R.layout.activity_courses_list_item,
	    			R.id.course_name_textview, courses);
	    }

		@NonNull
		public View getView(int position, View convertView, @NonNull ViewGroup parent) {
	        View v = super.getView(position, convertView, parent);
	        
	        Course course = mCourses.get(position);
	        
	        TextView textView = (TextView) v.findViewById(R.id.course_name_textview);
            //textView.setTypeface(typeface);
            textView.setText(course.getName() + " (" + String.valueOf(course.getHours()) + ")");
            
            textView = (TextView) v.findViewById(R.id.course_semester_textview);
            
            // Set list item to color of semester
            if (course.getSemesterID() != -1) {
            	Semester semester = IuvoApplication.db.getSemester(course.getSemesterID());
            	textView.setText(semester.getName());
            	v.setBackgroundColor(ColorHandler.getColor(getContext(), semester.getColor()));
            } else
            	v.setBackgroundColor(ColorHandler.getColor(getContext(), "gray"));
	        
            textView = (TextView) v.findViewById(R.id.course_grade_textview);
            textView.setTypeface(IuvoApplication.typeface);
            if (course.getGrade().equals("None"))
            	textView.setText("");
            else
            	textView.setText(course.getGrade());
            
	        View dragHandleView = v.findViewById(R.id.drag_handle);
	        LayoutParams params = (LayoutParams) dragHandleView.getLayoutParams();
	        params.height = 0;
	        params.width = 0;
	        params.weight = 0.0f;
	        dragHandleView.setLayoutParams(params);
	        
	        return v;
	    }
	}
	
	/**
	 * Variables
	 */
	
	private int semesterID;
	
	private ArrayList<Course> mCourses;
	
	private CoursesAdapter coursesAdapter;
	
	// Whenever a user swipes an course item left or right to delete.
    private final DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
		
		@Override
		public void remove(int which) {
			deleteCourseConfirm(which);			
		}
	};
	
	/**
	 * Overrides
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courses_activity_semester);
		// Show the Up button in the action bar.
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);

		}
	    
	    // Set up our drag sort ListView
	 	DragSortListView dragSortListView = (DragSortListView) getListView();
	 	dragSortListView.setRemoveListener(onRemove);
	 		
	 	Intent intent = getIntent();
		semesterID = intent.getIntExtra(SemestersActivity.SEMESTERS_ACTIVITY_SEMESTER_ID, -1);
	 	
	 	refreshListAdapter();
	 	
	 	setTitle(IuvoApplication.db.getSemester(semesterID).getName());
	}
    	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.courses_activity_semester, menu);
		return true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		refreshListAdapter();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(getApplicationContext(), CourseActivity.class);
		intent.putExtra(MainActivity.MAIN_ACTIVITY_COURSE_ID, mCourses.get(position).getID());
		startActivity(intent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.menu_courses_semester_help:
			menuCoursesHelp();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Voids
	 */
	// Pops up with dialog so that user get help
    private void menuCoursesHelp() {
    	Intent intent = new Intent(getApplicationContext(), CoursesHelpActivity.class);
    	startActivity(intent);
    }
	
	// Pops up with dialog so that user can confirm deletion
    private void deleteCourseConfirm(int which) {
    	Dialogs.deleteCourseConfirmSemester(this, which);
    }
    
    // Executes after user has confirmed that they want to delete a course
 	public void deleteCourse(int which) {

 		Course item = mCourses.get(which);
 		
 		// Remove course from database
 		IuvoApplication.db.deleteCourse(item);
 					
 		refreshListAdapter();
 	}	
	
	// Called whenever the ListView needs to be updated to reflect database changes
	public void refreshListAdapter() {
		
		// Refresh the ListAdapter to reflect the new changes in the database.
		List<Course> coursesInSemester = IuvoApplication.db.getAllCoursesBySemester(semesterID);
		
		Log.d("Semester: ", "Updating ListAdapter...");
		mCourses = new ArrayList<Course>();
		for (Course c : coursesInSemester) {
			Log.d("All Courses Semester", "Position: " + c.getPosition() + ", ID: " + c.getID() + ", gID: "+ c.getGroupID() + ", sID: " + c.getSemesterID() + ", Name: " + c.getName());
			mCourses.add(c);
		}
		
		coursesAdapter = new CoursesAdapter(mCourses);
		setListAdapter(coursesAdapter);
	}
}
