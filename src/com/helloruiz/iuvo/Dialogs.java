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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.helloruiz.iuvo.MainActivity.MeSectionFragment;
import com.helloruiz.iuvo.database.Group;
import com.helloruiz.iuvo.database.Semester;

/**
 * Stores all dialogs. Lots of lines just do display dialogs, but I'm sure that it's cleaner
 * to isolate them all and do the heavy lifting here than to pollute all of the other code.
 */
public class Dialogs {
	
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
	 * Dialog for 'About' grid item in the 'more' tab
	 */
	public static void groupsHelp(Activity activity) {
		
		// Took hours of looking for solutions, but found one that makes clickable links in dialogs
		// without having to create a new layout and inflate it, or any other tedious task.
		// http://stackoverflow.com/questions/7479813/android-linkify-text-in-dialog
		
		String dialogAboutTitle = "Groups Help";
		String dialogAbout = 
				"Not all degree plans are created the same. Some categorize your classes by subject (e.g. Math, Science, History, etc.) while others categorize them by classification (e.g. Freshman, Junior, etc.). Iuvo allows you to create groups in order to match your degree plan as closely as possible. You need to make at least one group before adding a course.\n\n" +
				"Add a group from the menu at the top\n\n" +
				"Delete a group by swiping it left or right. This will delete any classes that are in that group.\n\n" +
				"Re-arrange your groups by dragging their handles on the right side.\n\n" +
				"Edit a group by tapping on it.";
		
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
	 * Dialog for 'add' menu option in the group manager activity.
	 */
	public static void addGroup(final Activity activity) {
		
		String dialogAddTitle = "Add Group";
		String dialogName = activity.getString(R.string.dialog_name);
		
		final EditText editText = new EditText(activity);
		editText.setPadding(15, 15, 15, 15);
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
	
	/** 
	 * Dialog for allowing user to edit a group in the group manager activity.
	 */
	public static void editGroup(final Activity activity, final Group item) {
		
		String dialogAddTitle = "Edit Group";
		String dialogName = activity.getString(R.string.dialog_name);
		
		final EditText editText = new EditText(activity);
		editText.setPadding(15, 15, 15, 15);
		editText.setHint((CharSequence) dialogName);
		editText.setText((CharSequence) item.getName());
		
		// Listener for the 'Save' button
		DialogInterface.OnClickListener editClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Adds group to the database
				((GroupsActivity) activity).editGroup(editText.getText().toString(), item);
			}
		};
	    
		final AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
			.setPositiveButton("Save", editClickListener)
			.setNegativeButton("Cancel", null)
			.setTitle(dialogAddTitle)
			.setView(editText);
		
		dialog.show();
	}
	
	/** 
	 * Dialog for confirming deletion of a group
	 */
	public static void deleteGroupConfirm(final Activity activity, final int _which) {
		
		// Took hours of looking for solutions, but found one that makes clickable links in dialogs
		// without having to create a new layout and inflate it, or any other tedious task.
		// http://stackoverflow.com/questions/7479813/android-linkify-text-in-dialog
		
		String dialogAddTitle = activity.getString(R.string.dialog_delete_confirm_title);
		String dialogText = activity.getString(R.string.dialog_delete_confirm_text);
		
		TextView textView = new TextView(activity);
		textView.setText(dialogText);
		textView.setPadding(15, 15, 15, 15);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		
		// Listener for the 'Delete' button
		DialogInterface.OnClickListener deleteClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((GroupsActivity) activity).deleteGroup(_which);
			}
		};
		
		// Listener for the 'Cancel button
		DialogInterface.OnClickListener cancelClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((GroupsActivity) activity).refreshListAdapter();
			}
		};
	    
		final AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
			.setPositiveButton("Delete", deleteClickListener)
			.setNegativeButton("Cancel", cancelClickListener)
			.setTitle(dialogAddTitle)
			.setView(textView);
		
		dialog.show();
	}
	
	/** 
	 * Dialog for 'About' grid item in the 'more' tab
	 */
	public static void semestersHelp(Activity activity) {
		
		// Took hours of looking for solutions, but found one that makes clickable links in dialogs
		// without having to create a new layout and inflate it, or any other tedious task.
		// http://stackoverflow.com/questions/7479813/android-linkify-text-in-dialog
		
		String dialogAboutTitle = "Semesters Help";
		String dialogAbout = 
				"Iuvo allows you to create semesters in order to match your college career path as closely as possible. You can name your semesters whatever you want (e.g. \"Fall 2010\". \"SU 11\" or \"May Minimester 2012\") and order them however you like. In addition, you can pick a color for your semester. Classes assigned to a semester will be displayed in the semester’s color on your degree plan.\n\n" +
				"Add a semester from the menu at the top\n\n" +
				"Delete a semester by swiping it left or right. This will delete any classes that are in that semester.\n\n" +
				"Re-arrange your semesters by dragging their handles on the right side.\n\n" +
				"Edit a semester by tapping on it.";
		
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
	 * Dialog for 'add' menu option in the semester manager activity.
	 */
	public static void addSemester(final Activity activity) {
		
		String dialogAddTitle = "Add Semester";
		String dialogName = activity.getString(R.string.dialog_name);
		

		final EditText editText = new EditText(activity);
		final Spinner spinner = (Spinner) new Spinner(activity);
		LinearLayout linearLayout = new LinearLayout(activity);
		
		editText.setHint((CharSequence) dialogName);
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
		        R.array.color_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setSelection(adapter.getPosition("Select Color"));
		
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.addView(editText);
		linearLayout.addView(spinner);
		
		// Listener for the 'Save' button
		DialogInterface.OnClickListener addClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Adds semester to the database
				((SemestersActivity) activity).addSemester(editText.getText().toString(), spinner.getSelectedItem().toString());
			}
		};
	    
		final AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
			.setPositiveButton("Add", addClickListener)
			.setNegativeButton("Cancel", null)
			.setTitle(dialogAddTitle)
			.setView(linearLayout);
		
		dialog.show();
	}
	
	/** 
	 * Dialog for allowing user to edit a semester in the semester manager activity.
	 */
	public static void editSemester(final Activity activity, final Semester item) {
		
		String dialogAddTitle = "Edit Semester";
		String dialogName = activity.getString(R.string.dialog_name);
		

		final EditText editText = new EditText(activity);
		final Spinner spinner = (Spinner) new Spinner(activity);
		LinearLayout linearLayout = new LinearLayout(activity);
		
		editText.setHint((CharSequence) dialogName);
		editText.setText((CharSequence) item.getName());
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
		        R.array.color_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setSelection(adapter.getPosition(item.getColor()));
		
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.addView(editText);
		linearLayout.addView(spinner);
		
		// Listener for the 'Save' button
		DialogInterface.OnClickListener editClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Adds semester to the database
				((SemestersActivity) activity).editSemester(editText.getText().toString(), spinner.getSelectedItem().toString(), item);
			}
		};
	    
		final AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
			.setPositiveButton("Save", editClickListener)
			.setNegativeButton("Cancel", null)
			.setTitle(dialogAddTitle)
			.setView(linearLayout);
		
		dialog.show();
	}
	
	/** 
	 * Dialog for confirming deletion of a semester
	 */
	public static void deleteSemesterConfirm(final Activity activity, final int _which) {
		
		// Took hours of looking for solutions, but found one that makes clickable links in dialogs
		// without having to create a new layout and inflate it, or any other tedious task.
		// http://stackoverflow.com/questions/7479813/android-linkify-text-in-dialog
		
		String dialogAddTitle = activity.getString(R.string.dialog_delete_confirm_title);
		String dialogText = activity.getString(R.string.dialog_delete_confirm_text);
		
		TextView textView = new TextView(activity);
		textView.setText(dialogText);
		textView.setPadding(15, 15, 15, 15);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		
		// Listener for the 'Delete' button
		DialogInterface.OnClickListener deleteClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((SemestersActivity) activity).deleteSemester(_which);
			}
		};
		
		// Listener for the 'Cancel button
		DialogInterface.OnClickListener cancelClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((SemestersActivity) activity).refreshListAdapter();
			}
		};
	    
		final AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
			.setPositiveButton("Delete", deleteClickListener)
			.setNegativeButton("Cancel", cancelClickListener)
			.setTitle(dialogAddTitle)
			.setView(textView);
		
		dialog.show();
	}
}
