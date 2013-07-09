package com.helloruiz.iuvo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.helloruiz.iuvo.database.Course;
import com.helloruiz.iuvo.database.DatabaseHandler;
import com.helloruiz.iuvo.database.Group;
import com.helloruiz.iuvo.database.Semester;
import com.helloruiz.iuvo.help.CourseHelpActivity;

public class CourseActivity extends Activity {
	
	/**
	 * Variables
	 */
	int id = -1;
	int position = -1;
	String name = "";
	int hours = 3;
	String grade = "None";
	String excludeFromGPA = "No";
	int semesterID = -1;
	int oldGroupID = -1;
	int groupID = -1;
	String semester = "None";
	String group = "None (Hidden)";
	
	// Typeface for pretty lobster font.
	Typeface typeface;

	/**
	 * Overrides
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/lobster.otf");
		
		setContentView(R.layout.activity_course);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		
		id = intent.getIntExtra(MainActivity.MAINACTIVITY_COURSE_ID, -1);
		
		if (id != -1) {
			DatabaseHandler db = new DatabaseHandler(this);
			Course course = db.getCourse(id);
			
			position = course.getPosition();
			name = course.getName();
			hours = course.getHours();
			grade = course.getGrade();
			if (course.getExcludedFromGPA() == 1)
				excludeFromGPA = "Yes";
			semesterID = course.getSemesterID();
			groupID = course.getGroupID();
			oldGroupID = course.getGroupID();
			if (semesterID != -1)
				semester = db.getSemester(semesterID).getName();
			if (groupID != -1) {
				group = db.getGroup(groupID).getName();
			}
			
			setTitle(name);
		}
		
		EditText editText = (EditText) findViewById(R.id.course_name_edittext); editText.setTypeface(typeface);
		editText.setText((CharSequence) name);
		
		TextView textView;
		textView = (TextView) findViewById(R.id.course_hours_textview); textView.setTypeface(typeface);
		textView.setText(String.valueOf(hours));
		
        textView = (TextView) findViewById(R.id.course_grade_textview); textView.setTypeface(typeface);
        textView.setText(grade);
        
        textView = (TextView) findViewById(R.id.course_exclude_from_gpa_textview); textView.setTypeface(typeface);
        textView.setText(excludeFromGPA);

        textView = (TextView) findViewById(R.id.course_group_textview); textView.setTypeface(typeface);
        textView.setText(group);
        
        textView = (TextView) findViewById(R.id.course_semester_textview); textView.setTypeface(typeface);
        textView.setText(semester);
        
        // Not sure what's causing these views to change random colors. This ensures that they stay blue.
        View view;
        view = findViewById(R.id.course_name_linear_layout); view.setBackgroundColor(getResources().getColor(R.color.theme_blue));
        view = findViewById(R.id.course_hours_linear_layout); view.setBackgroundColor(getResources().getColor(R.color.theme_blue));
        view = findViewById(R.id.course_grade_linear_layout); view.setBackgroundColor(getResources().getColor(R.color.theme_blue));
        view = findViewById(R.id.course_exclude_from_gpa_linear_layout); view.setBackgroundColor(getResources().getColor(R.color.theme_blue));
        view = findViewById(R.id.course_group_linear_layout); view.setBackgroundColor(getResources().getColor(R.color.theme_blue));
        view = findViewById(R.id.course_semester_linear_layout); view.setBackgroundColor(getResources().getColor(R.color.theme_blue));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.course, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.menu_course_help:
			menuCourseHelp();
			return true;
		case R.id.menu_course_save:
			addCourse();
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * -- Voids --
	 */
	public void menuCourseHelp() {
		Intent intent = new Intent(getApplicationContext(), CourseHelpActivity.class);
    	startActivity(intent);
	}
	
	public void addCourse() {
		DatabaseHandler db = new DatabaseHandler(this);
		
		
		
		EditText editText = (EditText) findViewById(R.id.course_name_edittext);
		
		String name = editText.getText().toString();
		
		int eFGPA = 0;
		if(excludeFromGPA.equals("Yes"))
			eFGPA = 1;
		else
			eFGPA = 0;
		
		if(id == -1)
			db.addCourse(name, hours, grade, eFGPA, semesterID, groupID);
		else {
			if (oldGroupID != groupID) {
				db.decrementCoursePositions(position + 1, oldGroupID);
				position = db.getCourseCountByGroup(groupID);
			}
			
			Course course = new Course(id, position, name, hours, grade, eFGPA, semesterID, groupID);
			db.updateCourse(course);
		}
		
		Log.d("All Courses", "All Current Courses:");
		List<Course> allCourses = db.getAllCourses();
		for(Course c : allCourses) {
			Log.d("All Courses", "Position: " + c.getPosition() + ", ID: " + c.getID() + ", gID: "+ c.getGroupID() + ", sID: " + c.getSemesterID() + ", Name: " + c.getName());
		}
		
		Log.d("All Courses Group", "Courses the same group:");
		List<Course> allCoursesGroup = db.getAllCoursesByGroup(groupID);
		for(Course c : allCoursesGroup) {
			Log.d("All Courses Group", "Position: " + c.getPosition() + ", ID: " + c.getID() + ", gID: "+ c.getGroupID() + ", sID: " + c.getSemesterID() + ", Name: " + c.getName());
		}
	}
	
	public void editHours(View view) {
		String dialogTitle = getString(R.string.dialog_hours);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(dialogTitle)
	           .setItems(R.array.hours_array, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   hours = which + 1;
	            	   TextView textView;
	            	   textView = (TextView) findViewById(R.id.course_hours_textview);
	            	   textView.setText(String.valueOf(hours));
	           }
	    });
	    
	    builder.show();
	}
	
	public void editGrade(View view) {
		String dialogTitle = getString(R.string.dialog_grade);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(dialogTitle)
	           .setItems(R.array.grade_array, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	               switch(which) {
	               case 1:
	            	   grade = "A ";
	            	   break;
	               case 2:
	            	   grade = "A-";
	            	   break;
	               case 3:
	            	   grade = "B+";
	            	   break;
	               case 4:
	            	   grade = "B ";
	            	   break;
	               case 5:
	            	   grade = "B-";
	            	   break;
	               case 6:
	            	   grade = "C+";
	            	   break;
	               case 7:
	            	   grade = "C ";
	            	   break;
	               case 8:
	            	   grade = "C-";
	            	   break;
	               case 9:
	            	   grade = "D+";
	            	   break;
	               case 10:
	            	   grade = "D ";
	            	   break;
	               case 11:
	            	   grade = "D-";
	            	   break;
	               case 12:
	            	   grade = "F ";
	            	   break;
	               default:
	            	   grade = "None";
	               }
	               
	               TextView textView;
            	   textView = (TextView) findViewById(R.id.course_grade_textview);
            	   textView.setText(grade);
	           }
	    });
	    
	    builder.show();
	}
	
	public void editExcludeFromGPA(View view) {
		String dialogTitle = getString(R.string.dialog_exclude_from_gpa);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(dialogTitle)
	           .setItems(R.array.exclude_from_gpa_array, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	               if (which == 0)
	            	   excludeFromGPA = "Yes";
	               else
	            	   excludeFromGPA = "No";
	               
	               TextView textView;
            	   textView = (TextView) findViewById(R.id.course_exclude_from_gpa_textview);
            	   textView.setText(excludeFromGPA);
	           }
	    });
	    
	    builder.show();
	}
	
	public void editGroup(View view) {
		String dialogTitle = getString(R.string.dialog_group);
		
		DatabaseHandler databaseHandler = new DatabaseHandler(this);
		List<Group> groupsInDatabase = databaseHandler.getAllGroups();
		List<String> groupNames = new ArrayList<String>();
		List<String> groupIDs = new ArrayList<String>();
		
		groupNames.add("None (Hidden)");
		groupIDs.add("-1");
		for (Group g : groupsInDatabase) {
			groupNames.add(g.getName());
			groupIDs.add(String.valueOf(g.getID()));
		}
		
		final CharSequence[] groupNameItems = groupNames.toArray(new CharSequence[groupNames.size()]);
		final CharSequence[] groupIDItems = groupIDs.toArray(new CharSequence[groupIDs.size()]);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(dialogTitle);
	    builder.setItems(groupNameItems, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   group = groupNameItems[which].toString();
	            	   groupID = Integer.parseInt((String) groupIDItems[which]);
	            	   
	            	   Log.d("Course Group: ", "Group Name: " + group);
	            	   Log.d("Course Group: ", "Group Reference Key: " + groupID);
	            	   
	            	   TextView textView;
	            	   textView = (TextView) findViewById(R.id.course_group_textview);
	            	   textView.setText(group);
	               }
	    });
	    
	    builder.show();
	}
	
	public void editSemester(View view) {
		String dialogTitle = getString(R.string.dialog_semester);
		
		DatabaseHandler databaseHandler = new DatabaseHandler(this);
		List<Semester> semestersInDatabase = databaseHandler.getAllSemesters();
		List<String> semesterNames = new ArrayList<String>();
		List<String> semesterIDs = new ArrayList<String>();
		
		semesterNames.add("None");
		semesterIDs.add("-1");
		for (Semester g : semestersInDatabase) {
			semesterNames.add(g.getName());
			semesterIDs.add(String.valueOf(g.getID()));
		}
		
		final CharSequence[] semesterNameItems = semesterNames.toArray(new CharSequence[semesterNames.size()]);
		final CharSequence[] semesterIDItems = semesterIDs.toArray(new CharSequence[semesterIDs.size()]);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(dialogTitle);
	    builder.setItems(semesterNameItems, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   semester = semesterNameItems[which].toString();
	            	   semesterID = Integer.parseInt((String) semesterIDItems[which]);
	            	   
	            	   Log.d("Course Semester: ", "Semester Name: " + semester);
	            	   Log.d("Course Semester: ", "Semester Reference Key: " + semesterID);
	            	   
	            	   TextView textView;
	            	   textView = (TextView) findViewById(R.id.course_semester_textview);
	            	   textView.setText(semester);
	               }
	    });
	    
	    builder.show();
	}
}
