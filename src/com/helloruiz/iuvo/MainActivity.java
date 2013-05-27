package com.helloruiz.iuvo;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

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
    
    /**
     * We'll use this do display any dialogs. All the heavy lifting done in DialogManager.java
     */
    static DialogDatabase dialogDatabase = new DialogDatabase();
    
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
     * More section fragment. (Dummy for now.)
     */
    public static class MoreSectionFragment extends Fragment {
    	
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            
        	View rootView = inflater.inflate(R.layout.fragment_more, container, false);

            GridView gridView = (GridView) rootView.findViewById(R.id.more_grid_view);
            gridView.setAdapter(new ImageAdapter(rootView.getContext()));
            
            return rootView;
        }
        
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        	
            View rootView = getView();
            GridView gridView = (GridView) rootView.findViewById(R.id.more_grid_view);
            // Declared as final so that it can be used in the class defined in the
            // gridView.setItemClickListener method.
        	final Activity activity = getActivity();
        	
        	gridView.setOnItemClickListener(
        		new OnItemClickListener() {
        			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {             	
                	
        				Intent webIntent;
        				List<ResolveInfo> activities;
        				Toast noWebApp = Toast.makeText(activity.getApplicationContext(), "No web apps found.", Toast.LENGTH_SHORT);
        				
        				switch(position) {
        				case 0: // About Iuvo
        					
        					// Display 'About' dialog
        					dialogDatabase.aboutIuvo(activity, activity.getLayoutInflater());
        					
        					break;
        				case 1: // View Source code
        					
        					webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ruizz/Iuvo/"));

        					// Verify that there is at least one compatible app to open a web page
        					activities = activity.getPackageManager().queryIntentActivities(webIntent, 0);
        					if(activities.size() > 0)
        						startActivity(webIntent);
        					else
        						noWebApp.show();
        					
        					break;
        				case 2: // Visit my website
        					
        					webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://helloruiz.com"));

        					// Verify that there is at least one compatible app to open a web page
        					activities = activity.getPackageManager().queryIntentActivities(webIntent, 0);
        					if(activities.size() > 0)
        						startActivity(webIntent);
        					else
        						noWebApp.show();
        					
        					break;
        				default:
        					break;
        				}
        					
        			}
            });
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
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home, menu);
		
		MenuItem mEditProfile = menu.findItem(R.id.menu_edit_profile);
		MenuItem mManageGroups = menu.findItem(R.id.menu_manage_groups);
		MenuItem mManageSemesters = menu.findItem(R.id.menu_manage_semesters);
		MenuItem mAddCourse = menu.findItem(R.id.menu_add_course);
		
		switch(currentTabIndex) {
		case 0:
			mManageGroups.setVisible(false);
			mManageSemesters.setVisible(false);
			mAddCourse.setVisible(false);
			break;
		case 1:
			mEditProfile.setVisible(false);
			break;
		default:
			mEditProfile.setVisible(false);
			mManageGroups.setVisible(false);
			mManageSemesters.setVisible(false);
			mAddCourse.setVisible(false);
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
	        	menuManageGroups();
	        	return true;
	        case R.id.menu_manage_semesters:
	        	menuManageSemesters();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
    
    /**
     * Pops up with a dialog so that user can edit their profile.
     */
    public void menuEditProfile() {
    	AlertDialog.Builder dialogBuilder = dialogDatabase.editProfile(this, this.getLayoutInflater(), iuvoSettings);
		dialogBuilder.show();
    }
    
    /**
     * Starts the group manager activity.
     */
    public void menuManageGroups() {
    	
    	Intent intent = new Intent(this, GroupsActivity.class);
		startActivity(intent);
    }
    
    /**
     * Starts the semester manager activity
     */
    public void menuManageSemesters() {
    	
    	Intent intent = new Intent(this, SemestersActivity.class);
		startActivity(intent);
    }
    
    /**
     * Start the preferences editor and commit the new changes.
     * Called by the 'Save' buton displayed by the 'Edit Profile' dialog.
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