package com.helloruiz.iuvo.help;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.helloruiz.iuvo.R;
import com.helloruiz.iuvo.database.IuvoApplication;

public class PointsActivity extends Activity {
	
	/**
	 * Variables
	 */
	EditText aPlusEditText;
	EditText aEditText;
	EditText aMinusEditText;
	EditText bPlusEditText;
	EditText bEditText;
	EditText bMinusEditText;
	EditText cPlusEditText;
	EditText cEditText;
	EditText cMinusEditText;
	EditText dPlusEditText;
	EditText dEditText;
	EditText dMinusEditText;
	
	/**
	 * Overrides
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_points);
		
		aPlusEditText = (EditText) findViewById(R.id.points_aplus_edittext);
		aEditText = (EditText) findViewById(R.id.points_a_edittext);
		aMinusEditText = (EditText) findViewById(R.id.points_aminus_edittext);
		bPlusEditText = (EditText) findViewById(R.id.points_bplus_edittext);
		bEditText = (EditText) findViewById(R.id.points_b_edittext);
		bMinusEditText = (EditText) findViewById(R.id.points_bminus_edittext);
		cPlusEditText = (EditText) findViewById(R.id.points_cplus_edittext);
		cEditText = (EditText) findViewById(R.id.points_c_edittext);
		cMinusEditText = (EditText) findViewById(R.id.points_cminus_edittext);
		dPlusEditText = (EditText) findViewById(R.id.points_dplus_edittext);
		dEditText = (EditText) findViewById(R.id.points_d_edittext);
		dMinusEditText = (EditText) findViewById(R.id.points_dminus_edittext);
	
		aPlusEditText.setTypeface(IuvoApplication.typeface);
		aEditText.setTypeface(IuvoApplication.typeface);
		aMinusEditText.setTypeface(IuvoApplication.typeface);
		bPlusEditText.setTypeface(IuvoApplication.typeface);
		bEditText.setTypeface(IuvoApplication.typeface);
		bMinusEditText.setTypeface(IuvoApplication.typeface);
		cPlusEditText.setTypeface(IuvoApplication.typeface);
		cEditText.setTypeface(IuvoApplication.typeface);
		cMinusEditText.setTypeface(IuvoApplication.typeface);
		dPlusEditText.setTypeface(IuvoApplication.typeface);
		dEditText.setTypeface(IuvoApplication.typeface);
		dMinusEditText.setTypeface(IuvoApplication.typeface);

		aPlusEditText.setText((CharSequence) String.valueOf(IuvoApplication.pointsAPlus));
		aEditText.setText((CharSequence) String.valueOf(IuvoApplication.pointsA));
		aMinusEditText.setText((CharSequence) String.valueOf(IuvoApplication.pointsAMinus));
		bPlusEditText.setText((CharSequence) String.valueOf(IuvoApplication.pointsBPlus));
		bEditText.setText((CharSequence) String.valueOf(IuvoApplication.pointsB));
		bMinusEditText.setText((CharSequence) String.valueOf(IuvoApplication.pointsBMinus));
		cPlusEditText.setText((CharSequence) String.valueOf(IuvoApplication.pointsCPlus));
		cEditText.setText((CharSequence) String.valueOf(IuvoApplication.pointsC));
		cMinusEditText.setText((CharSequence) String.valueOf(IuvoApplication.pointsCMinus));
		dPlusEditText.setText((CharSequence) String.valueOf(IuvoApplication.pointsDPlus));
		dEditText.setText((CharSequence) String.valueOf(IuvoApplication.pointsD));
		dMinusEditText.setText((CharSequence) String.valueOf(IuvoApplication.pointsDMinus));
		
		// Prevent the blocks from inexplicably changing colors
		findViewById(R.id.points_aplus_linearlayout).setBackgroundColor(getResources().getColor(R.color.theme_blue));
		findViewById(R.id.points_a_linearlayout).setBackgroundColor(getResources().getColor(R.color.theme_blue));
		findViewById(R.id.points_aminus_linearlayout).setBackgroundColor(getResources().getColor(R.color.theme_blue));
		findViewById(R.id.points_bplus_linearlayout).setBackgroundColor(getResources().getColor(R.color.theme_blue));
		findViewById(R.id.points_b_linearlayout).setBackgroundColor(getResources().getColor(R.color.theme_blue));
		findViewById(R.id.points_bminus_linearlayout).setBackgroundColor(getResources().getColor(R.color.theme_blue));
		findViewById(R.id.points_cplus_linearlayout).setBackgroundColor(getResources().getColor(R.color.theme_blue));
		findViewById(R.id.points_c_linearlayout).setBackgroundColor(getResources().getColor(R.color.theme_blue));
		findViewById(R.id.points_cminus_linearlayout).setBackgroundColor(getResources().getColor(R.color.theme_blue));
		findViewById(R.id.points_dplus_linearlayout).setBackgroundColor(getResources().getColor(R.color.theme_blue));
		findViewById(R.id.points_d_linearlayout).setBackgroundColor(getResources().getColor(R.color.theme_blue));
		findViewById(R.id.points_dminus_linearlayout).setBackgroundColor(getResources().getColor(R.color.theme_blue));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.points, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.menu_points_save:
			savePoints();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void savePoints() {
		
		IuvoApplication.hideKeyboard(this, aPlusEditText);
		
		String aPlusString = aPlusEditText.getText().toString();
		String aString = aEditText.getText().toString();
		String aMinusString = aMinusEditText.getText().toString();
		String bPlusString = bPlusEditText.getText().toString();
		String bString = bEditText.getText().toString();
		String bMinusString = bMinusEditText.getText().toString();
		String cPlusString = cPlusEditText.getText().toString();
		String cString = cEditText.getText().toString();
		String cMinusString = cMinusEditText.getText().toString();
		String dPlusString = dPlusEditText.getText().toString();
		String dString = dEditText.getText().toString();
		String dMinusString = dMinusEditText.getText().toString();
		
		// Make sure none were left blank
		if(aPlusString.length() == 0 || aString.length() == 0 || aMinusString.length() == 0 ||
				bPlusString.length() == 0 || bString.length() == 0 || bMinusString.length() == 0 ||
				cPlusString.length() == 0 || cString.length() == 0 || cMinusString.length() == 0 ||
				dPlusString.length() == 0 || dString.length() == 0 || dMinusString.length() == 0) {
			Toast.makeText(this, getResources().getString(R.string.toast_points_not_saved), Toast.LENGTH_LONG).show();
			return;
		}
		
		SharedPreferences.Editor iuvoSettingsEditor = IuvoApplication.settings.edit();
		iuvoSettingsEditor.putFloat("pointsAPlus", Float.parseFloat(aPlusString));
		iuvoSettingsEditor.putFloat("pointsA", Float.parseFloat(aString));
		iuvoSettingsEditor.putFloat("pointsAMinus", Float.parseFloat(aMinusString));
		iuvoSettingsEditor.putFloat("pointsBPlus", Float.parseFloat(bPlusString));
		iuvoSettingsEditor.putFloat("pointsB", Float.parseFloat(bString));
		iuvoSettingsEditor.putFloat("pointsBMinus", Float.parseFloat(bMinusString));
		iuvoSettingsEditor.putFloat("pointsCPlus", Float.parseFloat(cPlusString));
		iuvoSettingsEditor.putFloat("pointsC", Float.parseFloat(cString));
		iuvoSettingsEditor.putFloat("pointsCMinus", Float.parseFloat(cMinusString));
		iuvoSettingsEditor.putFloat("pointsDPlus", Float.parseFloat(dPlusString));
		iuvoSettingsEditor.putFloat("pointsD", Float.parseFloat(dString));
		iuvoSettingsEditor.putFloat("pointsDMinus", Float.parseFloat(dMinusString));
		
		iuvoSettingsEditor.apply();
		
		IuvoApplication.updatePoints();
		
		onBackPressed();
	}
	
	

}
