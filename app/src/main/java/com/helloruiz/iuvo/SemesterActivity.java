package com.helloruiz.iuvo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.helloruiz.iuvo.database.IuvoApplication;
import com.helloruiz.iuvo.database.Semester;
import com.helloruiz.iuvo.fragments.SemesterColorList;

public class SemesterActivity extends FragmentActivity implements SemesterColorList.HeadlineSelection {

	private int semesterID = -1;

	private Semester semester;

	private String[] colorArray;
	
	private String colorSelection;

	private EditText nameEditText;

	private View nameView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semester);
		// Show the Up button in the action bar.
		setupActionBar();
		
		// Set all necessary typefaces
		TextView textView = (TextView) findViewById(R.id.semester_name_header); textView.setTypeface(IuvoApplication.typeface);
		textView = (TextView) findViewById(R.id.semester_quick_fill); textView.setTypeface(IuvoApplication.typeface);
		textView = (TextView) findViewById(R.id.semester_color_header); textView.setTypeface(IuvoApplication.typeface);
		
		colorArray = getResources().getStringArray(R.array.color_array); 
		
		colorSelection = "None";
		
		nameView = findViewById(R.id.semester_name_linear_layout);
		
		nameEditText = (EditText) findViewById(R.id.semester_name_edittext);
		
		nameEditText.setTypeface(IuvoApplication.typeface);
		
		// Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_semester_color_list_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of ExampleFragment
            SemesterColorList semesterColorList = new SemesterColorList();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            semesterColorList.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_semester_color_list_container, semesterColorList).commit();
        }
        
        Intent intent = getIntent();
		semesterID = intent.getIntExtra(SemestersActivity.SEMESTERSACTIVITY_SEMESTER_ID, -1);
		
        View view = findViewById(R.id.semester_color_linear_layout); view.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.theme_blue));
        nameView.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.theme_blue));
        
		if (semesterID != -1) {
			semester = IuvoApplication.db.getSemester(semesterID);
			nameEditText.setText((CharSequence) semester.getName());
			nameView.setBackgroundColor(ColorHandler.getColor(this, semester.getColor()));
			colorSelection = semester.getColor();
			setTitle(semester.getName());
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public void colorSelected(int position) {
		
		IuvoApplication.hideKeyboard(this, nameEditText);
		nameEditText.clearFocus();
		colorSelection = colorArray[position];
		nameView.setBackgroundColor(ColorHandler.getColor(this, colorSelection));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.semester, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.menu_semester_save:
			saveSemester();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// Called by Quick Fill objects
	public void springQuickFill(View view){
		nameEditText.setText(this.getText(R.string.semester_quick_fill_spring));
		nameEditText.setSelection(nameEditText.getText().length());
		nameEditText.requestFocus();
	}
	
	public void summerQuickFill(View view){
		nameEditText.setText(this.getText(R.string.semester_quick_fill_summer));
		nameEditText.setSelection(nameEditText.getText().length());
		nameEditText.requestFocus();
	}
	
	public void fallQuickFill(View view){
		nameEditText.setText(this.getText(R.string.semester_quick_fill_fall));
		nameEditText.setSelection(nameEditText.getText().length());
		nameEditText.requestFocus();
	}
		
	public void winterQuickFill(View view){
		nameEditText.setText(this.getText(R.string.semester_quick_fill_winter));
		nameEditText.setSelection(nameEditText.getText().length());
		nameEditText.requestFocus();
	}
	
	// User hits the save button.
	private void saveSemester() {
		
		IuvoApplication.hideKeyboard(this, nameEditText);
		
		if (!colorSelection.equals("None") && !nameEditText.getText().toString().equals("")) {
			if (semesterID == -1) {
				IuvoApplication.db.addSemester(nameEditText.getText().toString(), colorSelection);
				colorSelection = "None";
				nameEditText.setText("");
				nameView.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.theme_blue));
				Toast.makeText(this, getResources().getString(R.string.semester_added), Toast.LENGTH_LONG).show();
			} else {
				semester.setName(nameEditText.getText().toString());
				semester.setColor(colorSelection);
				IuvoApplication.db.updateSemester(semester);
				Toast.makeText(this, getResources().getString(R.string.semester_updated), Toast.LENGTH_LONG).show();
				onBackPressed();
			}
		} else {
			Toast.makeText(this, getResources().getString(R.string.semester_not_added), Toast.LENGTH_LONG).show();
		}
	}
}
