package com.helloruiz.iuvo;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	/**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	
    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;
    // If you ever need to switch pages programatically. Could be useful: mViewPager.setCurrentItem(0);
    
    /**
     * For keeping track of the currently selected tab.
     */
    int currentTabIndex = 0;
    
    /**
     * Application preferences
     */
    static SharedPreferences iuvoSettings;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        
        // Retrieve user info from settings.
        iuvoSettings = getSharedPreferences("User", 0);
    }
	
	@Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) { }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
        currentTabIndex = tab.getPosition();
        
        // Calls onCreateOptionsMenu() again
        invalidateOptionsMenu();
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) { }

	/**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                	// Me
                    return new MeSectionFragment();
                case 1:
                	// Degree Plan
                	return new PlanSectionFragment();
                default:
                	// More
                    return new MoreSectionFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
            case 0:
            	return "Me";
            case 1:
            	return "Plan";
            default:
            	return "More";
            }
        }
    }
    
    /**
     * Me section fragment.
     */
    public static class MeSectionFragment extends Fragment {

    	View rootView;
    	
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_me, container, false);
            displayProfile();
            return rootView;
        }
        
        public void displayProfile() {
        	TextView textView;
            
        	String userName = iuvoSettings.getString("name", "");
            String userSchool = iuvoSettings.getString("school", "");
            String userMajor = iuvoSettings.getString("major", "");
            String userClassification = iuvoSettings.getString("classification", "");
            
            // Assign name according to application settings.
            textView = (TextView) rootView.findViewById(R.id.me_name);
            if(!userName.equals(""))
            	textView.setText((CharSequence) userName);
            else
            	textView.setText("No Name");
            
            // Assign school...
            textView = (TextView) rootView.findViewById(R.id.me_school);
            if(!userSchool.equals(""))
            	textView.setText((CharSequence) userSchool);
            else
            	textView.setText("No School");
            
            // Major
            textView = (TextView) rootView.findViewById(R.id.me_major);
            if(!userMajor.equals(""))
            	textView.setText((CharSequence) userMajor);
            else
            	textView.setText("No Major");
            
            // Classification
            textView = (TextView) rootView.findViewById(R.id.me_classification);
            if(!userClassification.equals(""))
            	textView.setText((CharSequence) userClassification);
            else
            	textView.setText("No Classification");
        }
    }
    
    /**
     * Plan section fragment. (Dummy for now.)
     */
    public static class PlanSectionFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_plan, container, false);
            return rootView;
        }
    }
    
    /**
     * More section fragment. (Dummy for now.)
     */
    public static class MoreSectionFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_more, container, false);
            return rootView;
        }
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home, menu);
		
		MenuItem menuEditProfile = menu.findItem(R.id.menu_edit_profile);
		MenuItem menuManageGroups = menu.findItem(R.id.menu_manage_groups);
		
		switch(currentTabIndex) {
		case 0:
			menuManageGroups.setVisible(false);
			break;
		case 1:
			menuEditProfile.setVisible(false);
			break;
		case 2:
			menuEditProfile.setVisible(false);
			menuManageGroups.setVisible(false);
			break;
		default:
			menuEditProfile.setVisible(false);
			menuManageGroups.setVisible(false);
			break;
		}
		
		return true;
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    
		// Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_edit_profile:
	            menuEditProfile();
	            return true;
	        case R.id.menu_manage_groups:
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
    
    /**
     * Pops up with a dialog so that user can edit their profile.
     */
    public void menuEditProfile() {
    	
		// Get the layout inflater, so we can grab our custom dialog view
		LayoutInflater inflater = this.getLayoutInflater();
		
		// Grab the view that contains the EditTexts and Spinner so that we can access it.
		View dialogView = inflater.inflate(R.layout.dialog_edit_profile, null);
		
		// Access the dialog items in the view
		final EditText editTextName = (EditText) dialogView.findViewById(R.id.dialog_name);
		final EditText editTextSchool = (EditText) dialogView.findViewById(R.id.dialog_school);
		final EditText editTextMajor = (EditText) dialogView.findViewById(R.id.dialog_major);
		final Spinner spinner = (Spinner) dialogView.findViewById(R.id.dialog_spinner_classification);
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
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
				// Saves all changes to the app.
				saveProfileChanges(editTextName.getText().toString(), 
						editTextSchool.getText().toString(), 
						editTextMajor.getText().toString(), 
						spinner.getSelectedItem().toString());
				
				// Refresh the 'me' fragment. Wasn't easy to figure out because of FragmentPageAdapter, but found a solution.
		    	// http://stackoverflow.com/questions/10022179/fragmentpageradapter-with-viewpager-and-two-fragments-go-to-the-first-from-the
		    	// Remember this tagging convention...
		    	MeSectionFragment meSectionFragment = (MeSectionFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":"+0);
		    	meSectionFragment.displayProfile();
			}
		};
		
		// Listener for the 'Cancel' button
		DialogInterface.OnClickListener cancelClickListener = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Handle Cancel. Don't really have to do anything...
			}
		};

		// Create a builder for the dialog
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("Edit Profile");
		dialogBuilder.setView(dialogView);
		dialogBuilder.setPositiveButton("Save", saveClickListener).setNegativeButton("Cancel", cancelClickListener);
		dialogBuilder.show();
    }
    
    /**
     * Start the preferences editor and commit the new changes.
     */
    public void saveProfileChanges(String name, String school, String major, String classification) {
    	
    	SharedPreferences.Editor iuvoSettingsEditor = iuvoSettings.edit();
    	iuvoSettingsEditor.putString("name", name);
    	iuvoSettingsEditor.putString("school", school);
    	iuvoSettingsEditor.putString("major", major);
    	if(!classification.equals("Select Classification"))
    		iuvoSettingsEditor.putString("classification", classification);
    	else
    		iuvoSettingsEditor.putString("classification", "");
    	iuvoSettingsEditor.commit();
    }
}
