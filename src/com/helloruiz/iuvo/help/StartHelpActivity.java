package com.helloruiz.iuvo.help;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.helloruiz.iuvo.R;

public class StartHelpActivity extends Activity {

	/**
	 * Variables
	 */
	// Typeface for pretty lobster font.
    static Typeface typeFace;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_help);
		// Show the Up button in the action bar.
		setupActionBar();
		
		typeFace = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/lobster.otf");
		
		TextView headerTextView = (TextView)findViewById(R.id.activity_plan_getting_started_header);
    	headerTextView.setTypeface(typeFace);
    	headerTextView = (TextView)findViewById(R.id.activity_plan_getting_started_semester_title);
    	headerTextView.setTypeface(typeFace);
    	headerTextView = (TextView)findViewById(R.id.activity_plan_getting_started_group_title);
    	headerTextView.setTypeface(typeFace);
    	headerTextView = (TextView)findViewById(R.id.activity_plan_getting_started_course_grade0);
    	headerTextView.setTypeface(typeFace);
    	headerTextView = (TextView)findViewById(R.id.activity_plan_getting_sample_group1);
    	headerTextView.setTypeface(typeFace);
    	headerTextView = (TextView)findViewById(R.id.activity_plan_getting_started_course_grade1);
    	headerTextView.setTypeface(typeFace);
    	headerTextView = (TextView)findViewById(R.id.activity_plan_getting_started_course_grade2);
    	headerTextView.setTypeface(typeFace);
    	headerTextView = (TextView)findViewById(R.id.activity_plan_getting_sample_group2);
    	headerTextView.setTypeface(typeFace);
    	headerTextView = (TextView)findViewById(R.id.activity_plan_getting_started_course_grade3);
    	headerTextView.setTypeface(typeFace);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_help, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
