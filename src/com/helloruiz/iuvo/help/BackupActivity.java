package com.helloruiz.iuvo.help;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.helloruiz.iuvo.Dialogs;
import com.helloruiz.iuvo.R;
import com.helloruiz.iuvo.database.DatabaseHandler;

public class BackupActivity extends Activity {

	/**
	 * Variables
	 */
	// Typeface for pretty lobster font.
    static Typeface typeFace;
    
    // DatabaseHandler for backup/restore. May take backup/restore out of DatabaseHandler one day.
    DatabaseHandler db;
    
    // We'll use this do display any dialogs. All the heavy lifting done in Dialogs.java
    static Dialogs dialogs = new Dialogs();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backup);
		// Show the Up button in the action bar.
		setupActionBar();
		
		db = new DatabaseHandler(this);
		typeFace = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/lobster.otf");
		
		TextView textView;
		textView = (TextView) findViewById(R.id.backup_title_textview); textView.setTypeface(typeFace);
		textView = (TextView) findViewById(R.id.backup_backup_title_textview); textView.setTypeface(typeFace);
		textView = (TextView) findViewById(R.id.backup_restore_title_textview); textView.setTypeface(typeFace);
		
		// Not sure what's causing these views to change random colors. This ensures that they stay blue.
		View view;
        view = findViewById(R.id.backup_backup_linearlayout); view.setBackgroundColor(getResources().getColor(R.color.theme_blue));
        view = findViewById(R.id.backup_restore_linearlayout); view.setBackgroundColor(getResources().getColor(R.color.theme_blue));
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
		getMenuInflater().inflate(R.menu.backup, menu);
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

	public void backup(View view) {
		if (db.doesBackupExist()) {
			dialogs.exportConfirm(this);
		} else {
			try {
				db.exportDatabase();
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), view.getResources().getString(R.string.more_export_fail), Toast.LENGTH_LONG).show();
			} finally {
				Toast.makeText(getApplicationContext(), view.getResources().getString(R.string.more_export_success), Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public void restore(View view) {
		if (db.doesBackupExist()) {
			dialogs.importConfirm(this);
		} else {
			Toast.makeText(getApplicationContext(), view.getResources().getString(R.string.more_import_not_found), Toast.LENGTH_LONG).show();
		}
	}
}
