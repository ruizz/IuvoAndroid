package com.helloruiz.iuvo.help;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.helloruiz.iuvo.Dialogs;
import com.helloruiz.iuvo.R;
import com.helloruiz.iuvo.database.IuvoApplication;

public class BackupActivity extends Activity {
	
    /**
     * Overrides
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backup);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		TextView textView;
		textView = (TextView) findViewById(R.id.backup_title_textview); textView.setTypeface(IuvoApplication.typeface);
		textView = (TextView) findViewById(R.id.backup_backup_title_textview); textView.setTypeface(IuvoApplication.typeface);
		textView = (TextView) findViewById(R.id.backup_restore_title_textview); textView.setTypeface(IuvoApplication.typeface);
		
		// Not sure what's causing these views to change random colors. This ensures that they stay blue.
		View view;
        view = findViewById(R.id.backup_backup_linearlayout); view.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.theme_blue));
        view = findViewById(R.id.backup_restore_linearlayout); view.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.theme_blue));
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
	
	/**
	 * Voids
	 */
	public void backup(View view) {
		if (IuvoApplication.db.doesBackupExist()) {
			Dialogs.exportConfirm(this);
		} else {
			try {
				IuvoApplication.db.exportDatabase();
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), view.getResources().getString(R.string.more_export_fail), Toast.LENGTH_LONG).show();
			} finally {
				Toast.makeText(getApplicationContext(), view.getResources().getString(R.string.more_export_success), Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public void restore(View view) {
		if (IuvoApplication.db.doesBackupExist()) {
			Dialogs.importConfirm(this);
		} else {
			Toast.makeText(getApplicationContext(), view.getResources().getString(R.string.more_import_not_found), Toast.LENGTH_LONG).show();
		}
	}
}
