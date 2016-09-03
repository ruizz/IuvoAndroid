package com.helloruiz.iuvo;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.helloruiz.iuvo.MainActivity.MeSectionFragment;
import com.helloruiz.iuvo.database.Group;
import com.helloruiz.iuvo.database.IuvoApplication;

/**
 * Stores dialogs. Lots of lines just to display dialogs, but I'm sure that it's cleaner
 * to isolate them all and do the heavy lifting here than to pollute all of the other code.
 * Not all dialogs used in the app are located here. This is here to simply reduce some lines in
 * activities that have too much code.
 */
public class Dialogs {
	
	/** 
	 * SCRAPPED
	 * Dialog for the 'Edit Profile' menu setting that is shown when the 'me' tab is in focus.
	 * @return This one returns the dialog back to activity because the user may need to save some changes.
	 
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
				dialogBuilder.setPositiveButton("Save", saveClickListener).setNegativeButton(activity.getString(R.string.dialog_button_cancel), null);
				return dialogBuilder;
	}
	*/
	
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
			.setNegativeButton(activity.getString(R.string.dialog_button_cancel), null)
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
			.setNegativeButton(activity.getString(R.string.dialog_button_cancel), null)
			.setTitle(dialogAddTitle)
			.setView(editText);
		
		dialog.show();
	}
	
	/** 
	 * Dialog for confirming deletion of a group
	 */
	public static void deleteGroupConfirm(final Activity activity, final int _which) {
		
		String dialogAddTitle = activity.getString(R.string.dialog_delete_confirm_title);
		String dialogText = activity.getString(R.string.dialog_delete_group_confirm_text);
		
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
		
		// If the dialog is dismissed any other way. (Such as through the back button.)
		DialogInterface.OnCancelListener backClickListener = new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				((GroupsActivity) activity).refreshListAdapter();	
			}
		};
	    
		final AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
			.setPositiveButton(activity.getString(R.string.dialog_button_delete), deleteClickListener)
			.setNegativeButton(activity.getString(R.string.dialog_button_cancel), cancelClickListener)
			.setOnCancelListener(backClickListener)
			.setTitle(dialogAddTitle)
			.setView(textView);
		
		dialog.show();
	}
	
	/** 
	 * Dialog for confirming deletion of a semester
	 */
	public static void deleteSemesterConfirm(final Activity activity, final int _which) {
		
		String dialogAddTitle = activity.getString(R.string.dialog_delete_confirm_title);
		String dialogText = activity.getString(R.string.dialog_delete_semester_confirm_text);
		
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
		
		// If the dialog is dismissed any other way. (Such as through the back button.)
		DialogInterface.OnCancelListener backClickListener = new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				((SemestersActivity) activity).refreshListAdapter();	
			}
		};
	    
		final AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
			.setPositiveButton(activity.getString(R.string.dialog_button_delete), deleteClickListener)
			.setNegativeButton(activity.getString(R.string.dialog_button_cancel), cancelClickListener)
			.setOnCancelListener(backClickListener)
			.setTitle(dialogAddTitle)
			.setView(textView);
		
		dialog.show();
	}
	
	/** 
	 * Dialog for confirming deletion of a course
	 */
	public static void deleteCourseConfirm(final Activity activity, final int _which) {
		
		String dialogAddTitle = activity.getString(R.string.dialog_delete_confirm_title);
		String dialogText = activity.getString(R.string.dialog_delete_course_confirm_text);
		
		TextView textView = new TextView(activity);
		textView.setText(dialogText);
		textView.setPadding(15, 15, 15, 15);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		
		// Listener for the 'Delete' button
		DialogInterface.OnClickListener deleteClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((CoursesActivity) activity).deleteCourse(_which);
			}
		};
		
		// Listener for the 'Cancel button
		DialogInterface.OnClickListener cancelClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((CoursesActivity) activity).refreshListAdapter();
			}
		};
		
		// If the dialog is dismissed any other way. (Such as through the back button.)
		DialogInterface.OnCancelListener backClickListener = new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				((CoursesActivity) activity).refreshListAdapter();	
			}
		};
	    
		final AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
			.setPositiveButton(activity.getString(R.string.dialog_button_delete), deleteClickListener)
			.setNegativeButton(activity.getString(R.string.dialog_button_cancel), cancelClickListener)
			.setOnCancelListener(backClickListener)
			.setTitle(dialogAddTitle)
			.setView(textView);
		
		dialog.show();
	}
	
	/** 
	 * Dialog for confirming deletion of a course. Band-aid copy for a new activity. Will
	 * come up with a less wasteful solution later.
	 */
	public static void deleteCourseConfirmSemester(final Activity activity, final int _which) {
		
		String dialogAddTitle = activity.getString(R.string.dialog_delete_confirm_title);
		String dialogText = activity.getString(R.string.dialog_delete_course_confirm_text);
		
		TextView textView = new TextView(activity);
		textView.setText(dialogText);
		textView.setPadding(15, 15, 15, 15);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		
		// Listener for the 'Delete' button
		DialogInterface.OnClickListener deleteClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((CoursesActivitySemester) activity).deleteCourse(_which);
			}
		};
		
		// Listener for the 'Cancel button
		DialogInterface.OnClickListener cancelClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((CoursesActivitySemester) activity).refreshListAdapter();
			}
		};
		
		// If the dialog is dismissed any other way. (Such as through the back button.)
		DialogInterface.OnCancelListener backClickListener = new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				((CoursesActivitySemester) activity).refreshListAdapter();	
			}
		};
	    
		final AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
			.setPositiveButton(activity.getString(R.string.dialog_button_delete), deleteClickListener)
			.setNegativeButton(activity.getString(R.string.dialog_button_cancel), cancelClickListener)
			.setOnCancelListener(backClickListener)
			.setTitle(dialogAddTitle)
			.setView(textView);
		
		dialog.show();
	}
	
	/** 
	 * Dialog for confirming export
	 */
	public static void exportConfirm(final Activity activity) {
		
		String dialogAddTitle = activity.getString(R.string.dialog_title_backup_found);
		String dialogText1 = activity.getString(R.string.dialog_backup_found_export);
		
		TextView textView = new TextView(activity);
		textView.setText(dialogText1);
		textView.setPadding(15, 15, 15, 15);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		
		// Listener for the 'export' button
		DialogInterface.OnClickListener exportClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				try {
					if (IuvoApplication.db.exportDatabase())
						Toast.makeText(activity, activity.getResources().getString(R.string.more_export_success), Toast.LENGTH_LONG).show();
					else
						Toast.makeText(activity, activity.getResources().getString(R.string.more_export_fail), Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					Toast.makeText(activity, activity.getResources().getString(R.string.more_export_fail) + " (IOException)", Toast.LENGTH_LONG).show();
				}
			}
		};
	    
		final AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
			.setPositiveButton(activity.getString(R.string.dialog_button_export), exportClickListener)
			.setNegativeButton(activity.getString(R.string.dialog_button_cancel), null)
			.setTitle(dialogAddTitle)
			.setView(textView);
		dialog.show();
	}
	
	/** 
	 * Dialog for confirming export
	 */
	public static void importConfirm(final Activity activity) {
		
		String dialogAddTitle = activity.getString(R.string.dialog_title_backup_found);
		String dialogText1 = activity.getString(R.string.dialog_backup_found_import);
		
		TextView textView = new TextView(activity);
		textView.setText(dialogText1);
		textView.setPadding(15, 15, 15, 15);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		
		// Listener for the 'import' button
		DialogInterface.OnClickListener importClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				try {
					if (IuvoApplication.db.importDatabase()) {
						Toast.makeText(activity, activity.getResources().getString(R.string.more_import_success), Toast.LENGTH_LONG).show();
					} else
						Toast.makeText(activity, activity.getResources().getString(R.string.more_import_fail), Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					Toast.makeText(activity, activity.getResources().getString(R.string.more_import_fail) + "IOException", Toast.LENGTH_LONG).show();
				} 
			}
		};
	    
		final AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
			.setPositiveButton(activity.getString(R.string.dialog_button_import), importClickListener)
			.setNegativeButton(activity.getString(R.string.dialog_button_cancel), null)
			.setTitle(dialogAddTitle)
			.setView(textView);
		dialog.show();
	}
}
