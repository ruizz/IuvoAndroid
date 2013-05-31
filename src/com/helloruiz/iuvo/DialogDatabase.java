package com.helloruiz.iuvo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.helloruiz.iuvo.MainActivity.MeSectionFragment;
import com.helloruiz.iuvo.database.DatabaseHandler;

/**
 * Stores all dialogs. Lots of lines just do display dialogs, but I'm sure that it's cleaner
 * to isolate them all and do the heavy lifting here than to pollute all of the other code.
 */
public class DialogDatabase {
	
	/** 
	 * Dialog for 'About' grid item in the 'more' tab
	 */
	public void aboutIuvo(Activity activity) {
		
		// Took hours of looking for solutions, but found one that makes clickable links in dialogs
		// without having to create a new layout and inflate it, or any other tedious task.
		// http://stackoverflow.com/questions/7479813/android-linkify-text-in-dialog
		
		String dialogAboutTitle = "About Iuvo";
		String dialogAbout = "Iuvo Version 1.0\n\n" +
				"Created by Ruiz Akpan\n\n" +
				"A simple, open-source application that allows students to monitor the progress of their degree plan.\n\n" +
				"Resources Used:\n" +
				"http://jgilfelt.github.io/android-actionbarstylegenerator/\n\n" +
				"http://appicontemplate.com/\n\n" +
				"http://www.colourlovers.com/palette/559067/\n\n" +
				"http://www.impallari.com/lobster/";
		
		final TextView textView=new TextView(activity);
		textView.setText(dialogAbout);
		textView.setPadding(15, 15, 15, 15);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		textView.setAutoLinkMask(Activity.RESULT_OK);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	    
		final AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
			.setPositiveButton(android.R.string.ok, null)
			.setTitle(dialogAboutTitle)
			.setView(textView);
		
		dialog.show();
	}
	
	/** 
	 * Dialog for the 'Edit Profile' menu setting that is shown when the 'me' tab is in focus.
	 * @return This one returns the dialog back to activity because the user may need to save some changes.
	 */
	public static AlertDialog.Builder editProfile(final Activity activity, LayoutInflater inflater, SharedPreferences iuvoSettings) {
				
				// Grab the view that contains the EditTexts and Spinner so that we can access it.
				View dialogView = inflater.inflate(R.layout.dialog_edit_profile, null);
				
				// Access the dialog items in the view
				final EditText editTextName = (EditText) dialogView.findViewById(R.id.dialog_name);
				final EditText editTextSchool = (EditText) dialogView.findViewById(R.id.dialog_school);
				final EditText editTextMajor = (EditText) dialogView.findViewById(R.id.dialog_major);
				final Spinner spinner = (Spinner) dialogView.findViewById(R.id.dialog_spinner_classification);
				
				// Create an ArrayAdapter using the string array and a default spinner layout
				ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
				        R.array.classification_array, android.R.layout.simple_spinner_item);
				// Specify the layout to use when the list of choices appears
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				// Apply the adapter to the spinner
				spinner.setAdapter(adapter);
				
				// Populate the input fields with existing values
				editTextName.setText((CharSequence) iuvoSettings.getString("name", ""));
				editTextSchool.setText((CharSequence) iuvoSettings.getString("school", ""));
				editTextMajor.setText((CharSequence) iuvoSettings.getString("major", ""));
				spinner.setSelection(adapter.getPosition(iuvoSettings.getString("classification", "Select Classification")));
				
				// Listener for the 'Save' button
				DialogInterface.OnClickListener saveClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Saves all changes to the app
						((MainActivity) activity).saveProfileChanges(editTextName.getText().toString(), 
								editTextSchool.getText().toString(), 
								editTextMajor.getText().toString(), 
								spinner.getSelectedItem().toString());
						
						// Refresh the 'me' fragment. Wasn't easy to figure out because of FragmentPageAdapter, but finally found a solution to acces that fragment.
				    	// http://stackoverflow.com/questions/10022179/fragmentpageradapter-with-viewpager-and-two-fragments-go-to-the-first-from-the
				    	// Remember this tagging convention...
				    	MeSectionFragment meSectionFragment = (MeSectionFragment) ((MainActivity) activity).getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":"+0);
				    	meSectionFragment.displayProfile();
					}
				};
				
				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
				dialogBuilder.setTitle("Edit Profile");
				dialogBuilder.setView(dialogView);
				dialogBuilder.setPositiveButton("Save", saveClickListener).setNegativeButton("Cancel", null);
				return dialogBuilder;
	}
	
	/** 
	 * Dialog for 'add' menu option in the group manager activity.
	 */
	public static void addGroup(final Activity activity) {
		
		// Took hours of looking for solutions, but found one that makes clickable links in dialogs
		// without having to create a new layout and inflate it, or any other tedious task.
		// http://stackoverflow.com/questions/7479813/android-linkify-text-in-dialog
		
		String dialogAddTitle = "Add Group";
		String dialogName = activity.getString(R.string.dialog_name);
		
		final EditText editText = new EditText(activity);
		editText.setHint((CharSequence) dialogName);
		
		// Listener for the 'Save' button
		DialogInterface.OnClickListener addClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Adds group to the database
				((GroupsActivity) activity).addGroup(editText.getText().toString());
			}
		};
	    
		final AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
			.setPositiveButton("Add", addClickListener)
			.setNegativeButton("Cancel", null)
			.setTitle(dialogAddTitle)
			.setView(editText);
		
		dialog.show();
	}
}
