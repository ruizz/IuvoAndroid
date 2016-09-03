 package com.helloruiz.iuvo.help;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.helloruiz.iuvo.R;
import com.helloruiz.iuvo.database.IuvoApplication;

public class CourseHelpActivity extends Activity {

	/**
	 * Overrides
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_help);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		TextView textView;
		textView = (TextView) findViewById(R.id.course_help_title_textview); textView.setTypeface(IuvoApplication.typeface);
		textView = (TextView) findViewById(R.id.course_credit_image_textview); textView.setTypeface(IuvoApplication.typeface);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.course_help, menu);
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
