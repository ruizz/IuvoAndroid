package com.helloruiz.iuvo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.helloruiz.iuvo.database.Course;
import com.helloruiz.iuvo.database.DatabaseHandler;
import com.helloruiz.iuvo.database.Group;
import com.helloruiz.iuvo.database.Semester;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	/**
	 * Variables
	 */
    //The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
    //three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
    //derivative, which will keep every loaded fragment in memory. If this becomes too memory
    //intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	
    // The {@link ViewPager} that will display the three primary sections of the app, one at a time.
    // If you ever need to switch pages programatically. Could be useful: mViewPager.setCurrentItem(0);
    ViewPager mViewPager;
    
    // For keeping track of the currently selected tab.
    int currentTabIndex = 0;
    
    // Application preferences
    static SharedPreferences iuvoSettings;
    
    // Unique tags for passing an intent to another activity.
    static String MAINACTIVITY_COURSE_ID = "com.helloruiz.iuvo.MainActivity.courseID";
    static String MAINACTIVITY_GROUP_ID = "com.helloruiz.iuvo.MainActivity.groupID";
    static String MAINACTIVITY_EMPTY_GROUP_KEY = "com.helloruiz.iuvo.MainActivity.emptyGroupKey";
    
    // We'll use this do display any dialogs. All the heavy lifting done in DialogManager.java
    static Dialogs dialogs = new Dialogs();
    
    // Typeface for pretty lobster font.
    static Typeface typeFace;
    
    /**
     * Overrides
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        typeFace = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/lobster.otf");

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
	        case R.id.menu_add_course:
	        	menuAddCourse();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
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
            textView.setTypeface(typeFace);
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
	 * Plan section fragment.
	 */
	public static class PlanSectionFragment extends ListFragment {
	    
		
		// Static variables.
	    static List<String> groupTitles = new ArrayList<String>();
	    static List<Integer> groupIDs = new ArrayList<Integer>();
	    static List<String> courseTitles = new ArrayList<String>();
	    static List<String> courseSemesters = new ArrayList<String>();
	    static List<String> courseSemesterColors = new ArrayList<String>();
	    static List<String> courseGrades = new ArrayList<String>();
	    static List<Integer> courseIDs = new ArrayList<Integer>();
	    
	    private static final Integer LIST_HEADER = 0;
	    private static final Integer LIST_ITEM = 1;

	    private ListView listView;

	    // Called when MainActivity is first created.
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        
	    	listView = (ListView) inflater.inflate(R.layout.fragment_plan, container, false);
	        return listView;
	    }
	    
	    // Refreshes the fragment every time it comes back into focus.
	    @Override
	    public void onResume() {
	    	super.onResume();
	    	
	    	groupTitles.clear();
	    	groupIDs.clear();
	    	courseTitles.clear();
	    	courseSemesters.clear();
	    	courseSemesterColors.clear();
	    	courseGrades.clear();
	    	courseIDs.clear();

	    	DatabaseHandler db = new DatabaseHandler(getActivity());
	    	List<Group> groups = db.getAllGroups();
	    	
	    	// If user didn't create any groups, populate "Let's get started" dialogue into the headers and display.
	    	if (groups.size() == 0) {
	    		
	    		groupTitles.add(getString(R.string.plan_getting_started_header));
	    		groupTitles.add(getString(R.string.plan_getting_started_note1));
	    		groupTitles.add(getString(R.string.plan_getting_started_note2));
	    		
	    		listView = getListView(); 
		        setListAdapter(new getStartedListAdapter(getActivity()));
		        
	    	} else { // User created groups, populate the appropriate headers, titles, etc and display them.
	    		
		    	for (Group g : groups) {
		    		
		    		groupTitles.add(g.getName());
		    		groupIDs.add(g.getID());
		    		courseTitles.add(null);
		    		courseSemesters.add(null);
		    		courseSemesterColors.add(null);
		    		courseGrades.add(null);
		    		courseIDs.add(null);
		    		
		    		if (db.getCourseCountByGroup(g.getID()) == 0) {
		    			groupTitles.add(MAINACTIVITY_EMPTY_GROUP_KEY);
			    		groupIDs.add(g.getID());
			    		courseTitles.add(null);
			    		courseSemesters.add(null);
			    		courseSemesterColors.add(null);
			    		courseGrades.add(null);
			    		courseIDs.add(null);
		    		}
		    		
		    		List<Course> courses = db.getAllCoursesByGroup(g.getID());
		    		for (Course c : courses) {
		    			
		    			groupTitles.add(null);
		    			groupIDs.add(null);
			    		courseTitles.add(c.getName() + " (" + c.getHours() + ")");
			    		
			    		if (c.getSemesterID() == -1) {
			    			courseSemesters.add(getString(R.string.plan_no_semester_assigned));
			    			courseSemesterColors.add("Gray");
			    		} else {
			    			Semester semester = db.getSemester(c.getSemesterID());
			    			courseSemesters.add(semester.getName());
			    			courseSemesterColors.add(semester.getColor());
			    		}
			    			
			    		if (c.getGrade().equals("None"))
			    			courseGrades.add("");
			    		else
			    			courseGrades.add(c.getGrade());
			    		
			    		courseIDs.add(c.getID());
		    		}
		    	}
		    	
		    	listView = getListView();
		        setListAdapter(new DegreePlanListAdapter(getActivity()));
	    	}
	    }
	    
	    // BaseAdapter explicitly designed to show the "Let's get started" dialogue
	    private class getStartedListAdapter extends BaseAdapter {
	        public getStartedListAdapter(Context context) {
	            mContext = context;
	        }

	        @Override
	        public int getCount() { return groupTitles.size(); }

	        @Override
	        public Object getItem(int position) {return position;}

	        @Override
	        public long getItemId(int position) { return position; }

	        // Disables the highlight selection that appears when selecting a list item.
	        @Override
	        public boolean isEnabled(int position) {
	        	return false;
	        }

	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {

	            String headerText = groupTitles.get(position);
	            View rootView = convertView;
	            
                if (convertView == null || convertView.getId() == LIST_ITEM) {
                    rootView = LayoutInflater.from(mContext).inflate(
                            R.layout.fragment_plan_list_header, parent, false);
                    rootView.setId(LIST_HEADER);
                }
                
                TextView headerTextView = (TextView)rootView.findViewById(R.id.header_name_textview);
                headerTextView.setText(headerText);
                headerTextView.setGravity(Gravity.CENTER);
                
                // Title text
                if (position == 0) {
	                headerTextView.setTypeface(typeFace);
	                headerTextView.setTextSize(30);
	                headerTextView.setPadding(0, 0, 0, 5);
                } else { // Instructions text
                	headerTextView.setTextSize(18);
                	View dividerView = rootView.findViewById(R.id.item_separator);
                	dividerView.setVisibility(View.INVISIBLE);
                }
                
                headerTextView = (TextView) rootView.findViewById(R.id.header_edit_textview);
            	headerTextView.setVisibility(View.INVISIBLE);
            	
                return rootView;
	        }
	        private final Context mContext;
	    }
	    
		// Plan section fragment. Designed to show the degree plan as a list with headers.
	    private class DegreePlanListAdapter extends BaseAdapter {
	        public DegreePlanListAdapter(Context context) {
	            mContext = context;
	        }

	        @Override
	        public int getCount() {
	            return groupTitles.size();
	        }

	        @Override
	        public boolean areAllItemsEnabled() {
	            return true;
	        }

	        @Override
	        public boolean isEnabled(int position) {
	            return true;
	        }

	        @Override
	        public Object getItem(int position) {
	            return position;
	        }

	        @Override
	        public long getItemId(int position) {
	            return position;
	        }

	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {

	            String headerText = groupTitles.get(position);
	            View rootView = convertView;
	            
	            if(headerText != null) { // Group Header
	                if(convertView == null || convertView.getId() == LIST_ITEM) {
	                    rootView = LayoutInflater.from(mContext).inflate(
	                            R.layout.fragment_plan_list_header, parent, false);
	                    rootView.setId(LIST_HEADER);
	                }

	                TextView headerTextView = (TextView)rootView.findViewById(R.id.header_name_textview);
	                
	                if (headerText.equals(MAINACTIVITY_EMPTY_GROUP_KEY)) {
	                	headerTextView.setText(getString(R.string.plan_empty_group));
	                	headerTextView.setGravity(Gravity.CENTER);
	                	headerTextView.setTextSize(18);
	                	
	                	headerTextView = (TextView) rootView.findViewById(R.id.header_edit_textview);
	                	headerTextView.setVisibility(View.INVISIBLE);
	                	
	                	View dividerView = rootView.findViewById(R.id.item_separator);
	                	dividerView.setVisibility(View.INVISIBLE);
		            } else {
		            	headerTextView.setTypeface(typeFace);
		                headerTextView.setText(headerText);
		                
		                rootView.setTag(groupIDs.get(position));
		                
		                rootView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View view) {
								
								DatabaseHandler db = new DatabaseHandler(mContext);
								int groupID = (Integer) view.getTag();
								
								if (db.getCourseCountByGroup(groupID) == 0) {
									Toast.makeText(mContext, view.getResources().getString(R.string.plan_no_courses_found), Toast.LENGTH_LONG).show();
								} else {
									Intent intent = new Intent(mContext, CoursesActivity.class);
									intent.putExtra(MAINACTIVITY_GROUP_ID, groupID);
									startActivity(intent);
								}
							}
		                	
		                });
		            }
	                return rootView;
	            
	            } else { // Course Item
		            if(convertView == null || convertView.getId() == LIST_HEADER) {
		                rootView = LayoutInflater.from(mContext).inflate(
		                        R.layout.fragment_plan_list_item, parent, false);
		                rootView.setId(LIST_ITEM);
		            }
		            
		            TextView header = (TextView)rootView.findViewById(R.id.plan_item_title);
		            header.setText(courseTitles.get(position));
	
		            TextView subtext = (TextView)rootView.findViewById(R.id.plan_item_description);
		            subtext.setText(courseSemesters.get(position));

		            rootView.setBackgroundColor(ColorHandler.getColor(mContext, courseSemesterColors.get(position)));
		            
		            TextView gradeText = (TextView)rootView.findViewById(R.id.plan_item_grade);
		            
	                gradeText.setTypeface(typeFace);
		            gradeText.setText(courseGrades.get(position));
		            
		            rootView.setTag(courseIDs.get(position));
		            
		            rootView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {
							
							int courseID = (Integer) view.getTag();
							//Toast.makeText(mContext, "Course ID: " + courseID, Toast.LENGTH_LONG).show();
							Intent intent = new Intent(mContext, CourseActivity.class);
							intent.putExtra(MAINACTIVITY_COURSE_ID, courseID);
							startActivity(intent);
						}
	                	
	                });
		            return rootView;
	            }
	        }
	        private final Context mContext;
	    }
	}

	/**
     * More section fragment.
     */
	public static class MoreSectionFragment extends ListFragment {
    	
    	private ListView listView;
    	
    	public static final List<String> moreTitles = new ArrayList<String>();
    	public static final List<String> moreSubtitles = new ArrayList<String>();
    	
    	private static final Integer LIST_TITLE = 0;
    	
    	// Called when MainActivity is first created.
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        
	    	listView = (ListView) inflater.inflate(R.layout.fragment_plan, container, false);
	        return listView;
	    }
	    
	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	    	super.onActivityCreated(savedInstanceState);
	    	if (moreTitles.size() == 0) {
		    	String[] titles = getResources().getStringArray(R.array.fragment_more_titles_array);
		    	String[] subTitles = getResources().getStringArray(R.array.fragment_more_subtitles_array);
		    	
		    	for(int i = 0; i < titles.length; i++) {
		    		moreTitles.add(titles[i]);
		    		moreSubtitles.add(subTitles[i]);
		    	}
	    	}
	    	listView = getListView();
	        setListAdapter(new moreListAdapter(getActivity()));
	    }
        
	 // BaseAdapter explicitly designed to show the "Let's get started" dialogue
	    private class moreListAdapter extends BaseAdapter {
	        public moreListAdapter(Context context) {
	            mContext = context;
	        }

	        @Override
	        public int getCount() { return moreTitles.size(); }

	        @Override
	        public Object getItem(int position) {return position;}

	        @Override
	        public long getItemId(int position) { return position; }

	        // Disables the highlight selection that appears when selecting a list item.
	        @Override
	        public boolean isEnabled(int position) {
	        	return true;
	        }

	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {

	            String headerTitle = moreTitles.get(position);
	            String headerSubtitle = moreSubtitles.get(position);
	            View rootView = convertView;
	            
                if (convertView == null || convertView.getTag() == LIST_TITLE) {
                    rootView = LayoutInflater.from(mContext).inflate(
                            R.layout.fragment_more_list_item, parent, false);
                    rootView.setTag(LIST_TITLE);
                }
                
                TextView headerTextView = (TextView)rootView.findViewById(R.id.more_item_title);
                headerTextView.setText(headerTitle);
	            headerTextView.setTypeface(typeFace);
                
	            headerTextView = (TextView)rootView.findViewById(R.id.more_item_subtitle);
	            headerTextView.setText(headerSubtitle);
	            
	            switch(position) {
	            case 1:
	            	rootView.setBackgroundColor(getResources().getColor(R.color.theme_navy));
	            	break;
	            case 2:
	            	rootView.setBackgroundColor(getResources().getColor(R.color.theme_blue));
	            	break;
	            case 3:
	            	rootView.setBackgroundColor(getResources().getColor(R.color.theme_blue));
	            	break;
	            case 4:
	            	rootView.setBackgroundColor(getResources().getColor(R.color.theme_teal));
	            	break;
	            }
	            
	            rootView.setId(position);
	            
	            rootView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						
						int id = view.getId();
						Intent webIntent;
						List<ResolveInfo> activities;
						Toast noWebApp = Toast.makeText(getActivity().getApplicationContext(), "No web apps found.", Toast.LENGTH_LONG);
						DatabaseHandler db = new DatabaseHandler(getActivity());
						
						switch(id) {
						case 0: // View hidden courses
							
							if (db.getCourseCountByGroup(-1) == 0) {
								Toast.makeText(mContext, view.getResources().getString(R.string.plan_no_courses_found), Toast.LENGTH_LONG).show();
							} else {
								Intent intent = new Intent(mContext, CoursesActivity.class);
								intent.putExtra(MAINACTIVITY_GROUP_ID, -1);
								startActivity(intent);
							}
							
							break;
						case 1: // Display 'About' dialog
							
	    					dialogs.aboutIuvo(getActivity());
	    					
							break;
						case 2: // Export
							
							if (db.doesBackupExist()) {
								dialogs.exportConfirm(getActivity());
							} else {
								try {
									db.exportDatabase();
								} catch (IOException e) {
									Toast.makeText(mContext, view.getResources().getString(R.string.more_export_fail), Toast.LENGTH_LONG).show();
								} finally {
									Toast.makeText(mContext, view.getResources().getString(R.string.more_export_success), Toast.LENGTH_LONG).show();
								}
							}
							
							break;
						case 3: // Import
							
							if (db.doesBackupExist()) {
								dialogs.importConfirm(getActivity());
							} else {
								Toast.makeText(mContext, view.getResources().getString(R.string.more_import_not_found), Toast.LENGTH_LONG).show();
							}
							
							break;
						case 4: // Open browser, go to helloruiz.com
							
							webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://helloruiz.com"));

	    					// Verify that there is at least one compatible app to open a web page
	    					activities = getActivity().getPackageManager().queryIntentActivities(webIntent, 0);
	    					if(activities.size() > 0)
	    						startActivity(webIntent);
	    					else
	    						noWebApp.show();
	    					
							break;
						}
					}
                });
	            
                return rootView;
	        }

	        private final Context mContext;
	    }
    }
    
    /**
     * Pops up with a dialog so that user can edit their profile.
     */
    public void menuEditProfile() {
    	Dialogs.editProfile(this, this.getLayoutInflater(), iuvoSettings).show();
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
     * Starts the course editor activity
     */
    public void menuAddCourse() {
    	
    	Intent intent = new Intent(this, CourseActivity.class);
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