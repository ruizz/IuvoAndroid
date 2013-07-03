package com.helloruiz.iuvo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class CourseActivity extends Activity {
	
	/**
	 * Variables
	 */
	int hours = 3;
	String grade = "None";
	String excludeFromGPA = "No";
	int groupReferenceKey = -1;
	int semesterReferenceKey = -1;
	String group = "None (Hidden)";
	String semester = "None";

	/**
	 * Overrides
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Typeface typeFace=Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/lobster.otf");
		
		EditText editText = (EditText) findViewById(R.id.course_name_edittext); editText.setTypeface(typeFace);
		
		TextView textView;
		textView = (TextView) findViewById(R.id.course_hours_textview); textView.setTypeface(typeFace);
		textView.setText(String.valueOf(hours));
		
        textView = (TextView) findViewById(R.id.course_grade_textview); textView.setTypeface(typeFace);
        textView.setText(grade);
        
        textView = (TextView) findViewById(R.id.course_exclude_from_gpa_textview); textView.setTypeface(typeFace);
        textView.setText(excludeFromGPA);

        textView = (TextView) findViewById(R.id.course_group_textview); textView.setTypeface(typeFace);
        textView.setText(group);
        
        textView = (TextView) findViewById(R.id.course_semester_textview); textView.setTypeface(typeFace);
        textView.setText(semester);
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
			// TODO Show Dialog
			return true;
		case R.id.menu_course_save:
			addCourse();
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void addCourse() {
		DatabaseHandler db = new DatabaseHandler(this);
		
		int id = db.getMaxCourseID(groupReferenceKey) + 1;
		int gpa = 0;
		
		EditText editText = (EditText) findViewById(R.id.course_name_edittext);
		String name = editText.getText().toString();
		
		if(excludeFromGPA.equals("Yes"))
			gpa = 1;
		else
			gpa = 0;
		
		Course course = new Course(id, semesterReferenceKey, groupReferenceKey, name, hours, grade, gpa);
		
		db.addCourse(course);
		
		List<Course> allCourses = db.getAllCourses();
		for(Course c : allCourses) {
			Log.d("All Courses", "ID: " + c.getID() + ", gRK: "+ c.getGroupReferenceKey() + ", sRK: " + c.getSemesterReferenceKey() + ", Name: " + c.getName());
		}
		
		List<Course> allCoursesGroup = db.getAllCoursesByGroupReferenceKey(groupReferenceKey);
		for(Course c : allCoursesGroup) {
			Log.d("All Courses Group", "ID: " + c.getID() + ", gRK: "+ c.getGroupReferenceKey() + ", sRK: " + c.getSemesterReferenceKey() + ", Name: " + c.getName());
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
	            	   grade = "A";
	            	   break;
	               case 2:
	            	   grade = "A-";
	            	   break;
	               case 3:
	            	   grade = "B+";
	            	   break;
	               case 4:
	            	   grade = "B";
	            	   break;
	               case 5:
	            	   grade = "B-";
	            	   break;
	               case 6:
	            	   grade = "C+";
	            	   break;
	               case 7:
	            	   grade = "C";
	            	   break;
	               case 8:
	            	   grade = "C-";
	            	   break;
	               case 9:
	            	   grade = "D+";
	            	   break;
	               case 10:
	            	   grade = "D";
	            	   break;
	               case 11:
	            	   grade = "D-";
	            	   break;
	               case 12:
	            	   grade = "F";
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
		List<String> groupReferenceKeys = new ArrayList<String>();
		
		groupNames.add("None (Hidden)");
		groupReferenceKeys.add("-1");
		for (Group g : groupsInDatabase) {
			groupNames.add(g.getName());
			groupReferenceKeys.add(String.valueOf(g.getReferenceKey()));
		}
		
		final CharSequence[] groupNameItems = groupNames.toArray(new CharSequence[groupNames.size()]);
		final CharSequence[] groupReferenceKeyItems = groupReferenceKeys.toArray(new CharSequence[groupReferenceKeys.size()]);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(dialogTitle);
	    builder.setItems(groupNameItems, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   group = groupNameItems[which].toString();
	            	   groupReferenceKey = Integer.parseInt((String) groupReferenceKeyItems[which]);
	            	   
	            	   Log.d("Course Group: ", "Group Name: " + group);
	            	   Log.d("Course Group: ", "Group Reference Key: " + groupReferenceKey);
	            	   
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
		List<String> semesterReferenceKeys = new ArrayList<String>();
		
		semesterNames.add("None");
		semesterReferenceKeys.add("-1");
		for (Semester g : semestersInDatabase) {
			semesterNames.add(g.getName());
			semesterReferenceKeys.add(String.valueOf(g.getReferenceKey()));
		}
		
		final CharSequence[] semesterNameItems = semesterNames.toArray(new CharSequence[semesterNames.size()]);
		final CharSequence[] semesterReferenceKeyItems = semesterReferenceKeys.toArray(new CharSequence[semesterReferenceKeys.size()]);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(dialogTitle);
	    builder.setItems(semesterNameItems, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   semester = semesterNameItems[which].toString();
	            	   semesterReferenceKey = Integer.parseInt((String) semesterReferenceKeyItems[which]);
	            	   
	            	   Log.d("Course Semester: ", "Semester Name: " + semester);
	            	   Log.d("Course Semester: ", "Semester Reference Key: " + semesterReferenceKey);
	            	   
	            	   TextView textView;
	            	   textView = (TextView) findViewById(R.id.course_semester_textview);
	            	   textView.setText(semester);
	               }
	    });
	    
	    builder.show();
	}
}
