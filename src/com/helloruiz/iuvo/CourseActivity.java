package com.helloruiz.iuvo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.helloruiz.iuvo.database.Course;
import com.helloruiz.iuvo.database.Group;
import com.helloruiz.iuvo.database.IuvoApplication;
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
	String grade = "-";
	String excludeFromGPA = "No";
	int semesterID = -1;
	int oldGroupID = -1;
	int groupID = -1;
	String semester = "None";
	String group = "None";
	String semesterColor = "None";
	String groupSubText = "This course will be hidden.";
	String semesterSubText = "No semester assigned.";
	EditText nameEditText;
	
	/**
	 * Overrides
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		id = intent.getIntExtra(MainActivity.MAINACTIVITY_COURSE_ID, -1);
		
		if (id != -1) {
			Course course = IuvoApplication.db.getCourse(id);
			
			position = course.getPosition();
			name = course.getName();
			hours = course.getHours();
			if (!course.getGrade().equals("None"))
				grade = course.getGrade();
			if (course.getExcludedFromGPA() == 1)
				excludeFromGPA = "Yes";
			semesterID = course.getSemesterID();
			groupID = course.getGroupID();
			oldGroupID = course.getGroupID();
			if (semesterID != -1) {
				semester = IuvoApplication.db.getSemester(semesterID).getName();
				semesterColor = IuvoApplication.db.getSemester(semesterID).getColor();
				semesterSubText = String.valueOf(IuvoApplication.db.getCourseCountBySemester(semesterID)) + " Course";
				if (IuvoApplication.db.getCourseCountBySemester(semesterID) != 1)
					semesterSubText += "s";
			}
			if (groupID != -1) {
				group = IuvoApplication.db.getGroup(groupID).getName();
				groupSubText = String.valueOf(IuvoApplication.db.getCourseCountByGroup(groupID)) + " Course";
				if (IuvoApplication.db.getCourseCountByGroup(groupID) != 1)
					groupSubText += "s";
			}
			
			setTitle(name);
		}
		
		nameEditText = (EditText) findViewById(R.id.course_name_edittext);
		nameEditText.setText((CharSequence) name);
		
		TextView textView;
		textView = (TextView) findViewById(R.id.course_hours_textview); textView.setTypeface(IuvoApplication.typeface);
		textView.setText(String.valueOf(hours));
		
        textView = (TextView) findViewById(R.id.course_grade_textview); textView.setTypeface(IuvoApplication.typeface);
        textView.setText(grade);
        
        textView = (TextView) findViewById(R.id.course_exclude_from_gpa_textview); textView.setTypeface(IuvoApplication.typeface);
        textView.setText(excludeFromGPA);

        textView = (TextView) findViewById(R.id.course_group_textview); textView.setTypeface(IuvoApplication.typeface);
        textView.setText(group);
        
        textView = (TextView) findViewById(R.id.course_group_class_count_textview);
        textView.setText(groupSubText);
        
        textView = (TextView) findViewById(R.id.course_semester_textview); textView.setTypeface(IuvoApplication.typeface);
        textView.setText(semester);
        
        textView = (TextView) findViewById(R.id.course_semester_class_count_textview);
        textView.setText(semesterSubText);
        
        textView = (TextView) findViewById(R.id.course_name_header); textView.setTypeface(IuvoApplication.typeface);
        
        textView = (TextView) findViewById(R.id.course_details_header); textView.setTypeface(IuvoApplication.typeface);
        
        textView = (TextView) findViewById(R.id.course_group_header_textview); textView.setTypeface(IuvoApplication.typeface);
        
        textView = (TextView) findViewById(R.id.course_semester_header_textview); textView.setTypeface(IuvoApplication.typeface);
        
        // Not sure what's causing these views to change random colors. This ensures that they stay blue.
        View view;
        
        view = findViewById(R.id.course_name_linear_layout); view.setBackgroundColor(getResources().getColor(R.color.theme_blue));
	    view = findViewById(R.id.course_hours_linear_layout); view.setBackgroundColor(getResources().getColor(R.color.theme_blue));
	    view = findViewById(R.id.course_grade_linear_layout); view.setBackgroundColor(getResources().getColor(R.color.theme_blue));
	    view = findViewById(R.id.course_exclude_from_gpa_linear_layout); view.setBackgroundColor(getResources().getColor(R.color.theme_blue));
	    view = findViewById(R.id.course_group_linear_layout); view.setBackgroundColor(getResources().getColor(R.color.theme_blue));
	    if (groupID == -1) {
        	view.setBackgroundColor(getResources().getColor(R.color.gray));
        } else {
        	view.setBackgroundColor(getResources().getColor(R.color.theme_blue)); 
        }
	    
	    view = findViewById(R.id.course_semester_linear_layout); 
	    if (semesterID == -1) {
        	view.setBackgroundColor(getResources().getColor(R.color.gray));
        } else {
        	view.setBackgroundColor(ColorHandler.getColor(getApplicationContext(), semesterColor)); 
        }
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
		IuvoApplication.hideKeyboard(this, nameEditText);
		
		String name = nameEditText.getText().toString();
		if (name.equals("")) {
			Toast.makeText(this, getResources().getString(R.string.course_not_added), Toast.LENGTH_LONG).show();
			return;
		}
		
		int eFGPA = 0;
		if(excludeFromGPA.equals("Yes"))
			eFGPA = 1;
		else
			eFGPA = 0;
		
		if(id == -1) { // If user is adding a new course
			
			if (grade.equals("-"))
				grade = "None";
			
			// Add the course to the database
			IuvoApplication.db.addCourse(name, hours, grade, eFGPA, semesterID, groupID);
			
			// Clear all but the first word of the nameEditText
			int spaceIndex = name.indexOf(" ");
			if (spaceIndex == -1)
				nameEditText.setText("");
			else {
				nameEditText.setText((CharSequence) (name.substring(0, spaceIndex) + " "));
				nameEditText.setSelection(nameEditText.getText().length());
			}
			
			nameEditText.requestFocus();
			
			// Reset the grade. I feel that the others shouldn't be changed unless the user wants to change them.
			grade = "-";
			TextView textView;
     	   	textView = (TextView) findViewById(R.id.course_grade_textview);
     	   	textView.setText(grade);
     	   
     	   // Update the group and semester course counts
     	   if (semesterID != -1) {
				semesterSubText = String.valueOf(IuvoApplication.db.getCourseCountBySemester(semesterID)) + " Course";
				if (IuvoApplication.db.getCourseCountBySemester(semesterID) != 1)
					semesterSubText += "s";
				
				textView = (TextView) findViewById(R.id.course_semester_class_count_textview);
		        textView.setText(semesterSubText);
			}
			if (groupID != -1) {
				groupSubText = String.valueOf(IuvoApplication.db.getCourseCountByGroup(groupID)) + " Course";
				if (IuvoApplication.db.getCourseCountByGroup(groupID) != 1)
					groupSubText += "s";
				
				textView = (TextView) findViewById(R.id.course_group_class_count_textview);
		        textView.setText(groupSubText);
			}
     	   	
			Toast.makeText(this, getResources().getString(R.string.course_added), Toast.LENGTH_LONG).show();
		
		} else { // If user is updating an old course
			
			if (grade.equals("-"))
				grade = "None";
			
			if (oldGroupID != groupID) { // If user changed the group of the course
				IuvoApplication.db.decrementCoursePositions(position + 1, oldGroupID);
				position = IuvoApplication.db.getCourseCountByGroup(groupID);
			}
			
			Course course = new Course(id, position, name, hours, grade, eFGPA, semesterID, groupID);
			IuvoApplication.db.updateCourse(course);
			Toast.makeText(this, getResources().getString(R.string.course_updated), Toast.LENGTH_LONG).show();
		}
		
		Log.d("All Courses", "All Current Courses:");
		List<Course> allCourses = IuvoApplication.db.getAllCourses();
		for(Course c : allCourses) {
			Log.d("All Courses", "Position: " + c.getPosition() + ", ID: " + c.getID() + ", gID: "+ c.getGroupID() + ", sID: " + c.getSemesterID() + ", Name: " + c.getName());
		}
		
		if(id != -1) // If user is updating an old course, take them back.
			onBackPressed();
	}
	
	public void editHours(View view) {
		IuvoApplication.hideKeyboard(this, nameEditText);
		nameEditText.clearFocus();
		
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
		IuvoApplication.hideKeyboard(this, nameEditText);
		nameEditText.clearFocus();
		
		String dialogTitle = getString(R.string.dialog_grade);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(dialogTitle)
	           .setItems(R.array.grade_array, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	               switch(which) {
	               case 1:
	            	   grade = "A+";
	            	   break;
	               case 2:
	            	   grade = "A ";
	            	   break;
	               case 3:
	            	   grade = "A-";
	            	   break;
	               case 4:
	            	   grade = "B+";
	            	   break;
	               case 5:
	            	   grade = "B ";
	            	   break;
	               case 6:
	            	   grade = "B-";
	            	   break;
	               case 7:
	            	   grade = "C+";
	            	   break;
	               case 8:
	            	   grade = "C ";
	            	   break;
	               case 9:
	            	   grade = "C-";
	            	   break;
	               case 10:
	            	   grade = "D+";
	            	   break;
	               case 11:
	            	   grade = "D ";
	            	   break;
	               case 12:
	            	   grade = "D-";
	            	   break;
	               case 13:
	            	   grade = "F ";
	            	   break;
	               default:
	            	   grade = "-";
	               }
	               
	               TextView textView;
            	   textView = (TextView) findViewById(R.id.course_grade_textview);
            	   textView.setText(grade);
	           }
	    });
	    
	    builder.show();
	}
	
	public void editExcludeFromGPA(View view) {
		IuvoApplication.hideKeyboard(this, nameEditText);
		nameEditText.clearFocus();
		
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
		IuvoApplication.hideKeyboard(this, nameEditText);
		nameEditText.clearFocus();
		
		String dialogTitle = getString(R.string.dialog_group);
		
		List<Group> groupsInDatabase = IuvoApplication.db.getAllGroups();
		List<String> groupNames = new ArrayList<String>();
		List<String> groupIDs = new ArrayList<String>();
		
		groupNames.add("None");
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
	            	   
	            	   View view;
	            	   view = findViewById(R.id.course_group_linear_layout);
	            	   if (group.equals("None")) {
	            		   view.setBackgroundColor(getResources().getColor(R.color.gray));
	            		   groupSubText = "This course will be hidden.";
	            	   } else {
	            		   view.setBackgroundColor(getResources().getColor(R.color.theme_blue));
	            		   groupSubText = String.valueOf(IuvoApplication.db.getCourseCountByGroup(groupID)) + " Course";
	            		   if (IuvoApplication.db.getCourseCountByGroup(groupID) != 1)
	            			   groupSubText += "s";
	            	   } 
	            	   
	            	   textView = (TextView) findViewById(R.id.course_group_class_count_textview);
	            	   textView.setText(groupSubText);
	               }
	    });
	    
	    builder.show();
	}
	
	public void editSemester(View view) {
		IuvoApplication.hideKeyboard(this, nameEditText);
		nameEditText.clearFocus();
		
		String dialogTitle = getString(R.string.dialog_semester);
		
		List<Semester> semestersInDatabase = IuvoApplication.db.getAllSemesters();
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
	            	   
	            	   View view;
	            	   view = findViewById(R.id.course_semester_linear_layout);
	            	   if (semester.equals("None")) {
	            		   semesterColor = "None";
	            		   view.setBackgroundColor(getResources().getColor(R.color.gray));
	            		   semesterSubText = "No semester assigned.";
	            	   } else {
	            		   semesterColor = IuvoApplication.db.getSemester(semesterID).getColor();
	            		   view.setBackgroundColor(ColorHandler.getColor(getApplicationContext(), semesterColor)); 
	            		   semesterSubText = String.valueOf(IuvoApplication.db.getCourseCountBySemester(semesterID)) + " Course";
	            		   if (IuvoApplication.db.getCourseCountBySemester(semesterID) != 1)
	            			   semesterSubText += "s";
	            	   } 
	            	   
	            	   textView = (TextView) findViewById(R.id.course_semester_class_count_textview);
	            	   textView.setText(semesterSubText);
	               }
	    });
	    
	    builder.show();
	}
}
