package com.helloruiz.iuvo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CourseActivity extends Activity {
	
	static int hours = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Typeface typeFace=Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/lobster.otf");
		
		EditText editText = (EditText) findViewById(R.id.course_name_edittext); editText.setTypeface(typeFace);
		
		TextView textView;
		textView = (TextView) findViewById(R.id.course_hours_textview); textView.setTypeface(typeFace);
        textView = (TextView) findViewById(R.id.course_grade_textview); textView.setTypeface(typeFace);
        textView = (TextView) findViewById(R.id.course_exclude_from_gpa_textview); textView.setTypeface(typeFace);
        textView = (TextView) findViewById(R.id.course_group_textview); textView.setTypeface(typeFace);
        textView = (TextView) findViewById(R.id.course_semester_textview); textView.setTypeface(typeFace);
        
        
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	public void editHours(View view) {
		String dialogTitle = getString(R.string.dialog_hours);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(dialogTitle)
	           .setItems(R.array.hours_array, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	               hours = which + 1;
	           }
	    });
	    
	    builder.show();
	}
	
	public void editGrade(View view) {
		
	}
	
	public void editExcludedFromGPA(View view) {
		
	}
	
	public void editGroup(View view) {
		
	}
	
	public void editSemester(View view) {
		
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
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
