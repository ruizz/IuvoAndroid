package com.helloruiz.iuvo.help;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.helloruiz.iuvo.R;

public class SemestersHelpActivity extends Activity {

	/**
	 * Variables
	 */
	// Typeface for pretty lobster font.
    static Typeface typeFace;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semesters_help);
		// Show the Up button in the action bar.
		setupActionBar();
		
		typeFace = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/lobster.otf");
		
		TextView textView;
		textView = (TextView) findViewById(R.id.semester_help_title_textview); textView.setTypeface(typeFace);
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
		getMenuInflater().inflate(R.menu.semesters_help, menu);
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
