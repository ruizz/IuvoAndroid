package com.helloruiz.iuvo.help;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.helloruiz.iuvo.R;

public class AboutActivity extends Activity {

	/**
	 * Overrides
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Set the app version number, so that I don't have to every time I update the app. Convenience!
		try {
			String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			TextView textView = (TextView) findViewById(R.id.about_version_textview);
			textView.setText((CharSequence) textView.getText() + " " + version);
		} catch (NameNotFoundException e) {
			Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.toast_error_version), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
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
	public void website(View view) {
		Uri webpage = Uri.parse("http://helloruiz.com");
		Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);

		// Verify that there is at least one compatible app
		PackageManager packageManager = getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(webIntent, 0);

		if(activities.size() > 0) {
			startActivity(webIntent);
		} else {
			Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.toast_no_web), Toast.LENGTH_LONG).show();
			return;
		}
	}
	
	public void twitter(View view) {
		Uri webpage = Uri.parse("http://twitter.com/helloruiz");
		Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);

		// Verify that there is at least one compatible app
		PackageManager packageManager = getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(webIntent, 0);

		if(activities.size() > 0) {
			startActivity(webIntent);
		} else {
			Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.toast_no_web), Toast.LENGTH_LONG).show();
			return;
		}
	}
	
	public void email(View view) {
		Uri email = Uri.parse("mailto:ruiz.akpan@gmail.com");
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, email);

		// Verify that there is at least one compatible app
		PackageManager packageManager = getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(emailIntent, 0);

		if(activities.size() > 0) {
			startActivity(emailIntent);
		} else {
			Context context = getApplicationContext();
			Toast.makeText(context, getApplicationContext().getString(R.string.toast_no_email), Toast.LENGTH_LONG).show();
			return;
		}
	}
	
	public void rate(View view) {
		Uri webpage = Uri.parse("market://details?id=com.helloruiz.iuvo");
		Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);

		// Verify that there is at least one compatible app
		PackageManager packageManager = getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(webIntent, 0);

		if(activities.size() > 0) {
			startActivity(webIntent);
		} else {
			Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.toast_no_market), Toast.LENGTH_LONG).show();
			return;
		}
	}
	
	public void license(View view) {
		String dialogAboutTitle = getApplicationContext().getString(R.string.about_open_source);
		String dialogAbout = getApplicationContext().getString(R.string.about_dialog_license_content);
		
		final TextView textView=new TextView(this);
		textView.setText(dialogAbout);
		textView.setPadding(15, 15, 15, 15);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	    
		final AlertDialog.Builder dialog = new AlertDialog.Builder(this)
			.setPositiveButton(android.R.string.ok, null)
			.setTitle(dialogAboutTitle)
			.setView(textView);
		
		dialog.show();
	}
}
