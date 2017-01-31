package com.helloruiz.iuvo;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.helloruiz.iuvo.database.Course;
import com.helloruiz.iuvo.database.Group;
import com.helloruiz.iuvo.database.IuvoApplication;
import com.helloruiz.iuvo.database.Semester;
import com.helloruiz.iuvo.help.AboutActivity;
import com.helloruiz.iuvo.help.BackupActivity;
import com.helloruiz.iuvo.help.PointsActivity;
import com.helloruiz.iuvo.help.StartHelpActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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

    // Unique tags for passing an intent to another activity.
    static final String MAINACTIVITY_COURSE_ID = "com.helloruiz.iuvo.MainActivity.courseID";
    static final String MAINACTIVITY_GROUP_ID = "com.helloruiz.iuvo.MainActivity.groupID";
    static final String MAINACTIVITY_EMPTY_GROUP_KEY = "com.helloruiz.iuvo.MainActivity.emptyGroupKey";

    // Context.
    //static Context mContext;

    /**
     * Overrides
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mContext = getApplicationContext();

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
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
        currentTabIndex = tab.getPosition();

        // Calls onCreateOptionsMenu() again
        invalidateOptionsMenu();
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

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
            switch (position) {
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

        MenuItem mManageGroups = menu.findItem(R.id.menu_manage_groups);
        MenuItem mManageSemesters = menu.findItem(R.id.menu_manage_semesters);
        MenuItem mAddCourse = menu.findItem(R.id.menu_add_course);

        switch (currentTabIndex) {
            case 0:
                mManageGroups.setVisible(false);
                mManageSemesters.setVisible(false);
                mAddCourse.setVisible(false);
                setTitle("Iuvo");
                break;
            case 1:
                setTitle("");
                break;
            default:
                mManageGroups.setVisible(false);
                mManageSemesters.setVisible(false);
                mAddCourse.setVisible(false);
                setTitle("Iuvo");
                break;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            // Scrapping Profile Information
            //case R.id.menu_edit_profile:
            //menuEditProfile();
            //return true;
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

        @Override
        public void onResume() {
            super.onResume();
            displayProfile();
        }

        public void displayProfile() {
            TextView textView;
            View view;
            LayoutParams params;

            // Setting typeface for all headers
            textView = (TextView) rootView.findViewById(R.id.me_gpa_header);
            textView.setTypeface(IuvoApplication.typeface);
            textView = (TextView) rootView.findViewById(R.id.me_progress_header);
            textView.setTypeface(IuvoApplication.typeface);
            textView = (TextView) rootView.findViewById(R.id.me_grade_dist_header);
            textView.setTypeface(IuvoApplication.typeface);
            textView = (TextView) rootView.findViewById(R.id.me_database_header);
            textView.setTypeface(IuvoApplication.typeface);

        	/* Scrapping profile information 
            // TODO Get from string XML instead
        	String userName = IuvoApplication.settings.getString("name", "");
            String userSchool = IuvoApplication.settings.getString("school", "");
            String userMajor = IuvoApplication.settings.getString("major", "");
            String userClassification = IuvoApplication.settings.getString("classification", "");
            
            // Assign name according to application settings.
            textView = (TextView) rootView.findViewById(R.id.me_name);
            textView.setTypeface(IuvoApplication.typeface);
            if(!userName.equals(""))
            	textView.setText((CharSequence) userName + " ");
            else
            	textView.setText("No Name");
            
            // Assign school...
            textView = (TextView) rootView.findViewById(R.id.me_school);
            textView.setTypeface(IuvoApplication.typeface);
            if(!userSchool.equals(""))
            	textView.setText((CharSequence) userSchool + " ");
            else
            	textView.setText("No School");
            
            // Major
            textView = (TextView) rootView.findViewById(R.id.me_major);
            textView.setTypeface(IuvoApplication.typeface);
            if(!userMajor.equals(""))
            	textView.setText((CharSequence) userMajor + " ");
            else
            	textView.setText("No Major");
            
            // Classification
            textView = (TextView) rootView.findViewById(R.id.me_classification);
            textView.setTypeface(IuvoApplication.typeface);
            if(!userClassification.equals(""))
            	textView.setText((CharSequence) userClassification + " ");
            else
            	textView.setText("No Classification");
            	
            */

            // GPA
            String GPA = IuvoApplication.db.getGPA(false);
            String GPAWithExclusions = IuvoApplication.db.getGPA(true);

            textView = (TextView) rootView.findViewById(R.id.me_gpa);
            textView.setTypeface(IuvoApplication.typeface);
            textView.setText((CharSequence) GPA);

            textView = (TextView) rootView.findViewById(R.id.me_gpa_with_exclusions);
            textView.setTypeface(IuvoApplication.typeface);
            textView.setText((CharSequence) GPAWithExclusions);

            // Progress
            int courseCount = IuvoApplication.db.getCourseCountInDegreePlan();
            int courseCountCompleted = IuvoApplication.db.getCourseCountInDegreePlanCompleted();
            int courseCountAttempted = IuvoApplication.db.getCourseCountInDegreePlanAttempted();
            int courseHoursCompleted = IuvoApplication.db.getCompletedHours();
            int courseHoursTotal = IuvoApplication.db.getTotalHours();

            double completePercentage;
            if (courseCount == 0 && courseCountCompleted == 0)
                completePercentage = 0.0;
            else
                completePercentage = ((double) courseCountCompleted / (double) courseCount) * 100;

            DecimalFormat format = new DecimalFormat("###.#");
            String completePercentageString = format.format(completePercentage);

            textView = (TextView) rootView.findViewById(R.id.me_progress_percentage);
            textView.setTypeface(IuvoApplication.typeface);
            textView.setText(this.getString(R.string.me_progress_percentage, completePercentageString));

            textView = (TextView) rootView.findViewById(R.id.me_progress_fraction);
            textView.setTypeface(IuvoApplication.typeface);
            textView.setText(this.getString(R.string.me_progress_fraction, courseCountCompleted, courseCount));

            textView = (TextView) rootView.findViewById(R.id.me_progress_fraction_hours);
            textView.setTypeface(IuvoApplication.typeface);
            textView.setText(this.getString(R.string.me_progress_fraction, courseHoursCompleted, courseHoursTotal));

            if (courseCountCompleted == 0) { // Show 0% completion

                view = (View) rootView.findViewById(R.id.me_progress_completed);
                params = (LayoutParams) view.getLayoutParams();
                params.weight = 0.0f;
                view.setLayoutParams(params);
                view.setBackgroundColor(getResources().getColor(R.color.theme_teal));

                view = (View) rootView.findViewById(R.id.me_progress_not_completed);
                params = (LayoutParams) view.getLayoutParams();
                params.weight = 1.0f;
                view.setLayoutParams(params);
                view.setBackgroundColor(getResources().getColor(R.color.theme_blue));

            } else { // Show X% completion

                view = (View) rootView.findViewById(R.id.me_progress_completed);
                params = (LayoutParams) view.getLayoutParams();
                params.weight = (float) courseCountCompleted;
                view.setLayoutParams(params);
                view.setBackgroundColor(getResources().getColor(R.color.theme_teal));

                view = (View) rootView.findViewById(R.id.me_progress_not_completed);
                params = (LayoutParams) view.getLayoutParams();
                params.weight = (float) courseCount - (float) courseCountCompleted;
                view.setLayoutParams(params);
                view.setBackgroundColor(getResources().getColor(R.color.theme_blue));
            }

            // Grade distribution. This code to give the grade distribution bar its intended effect
            // looks like a horrible 'gum over the pipe leak' approach. If I find better methods, I'll change it.
            textView = (TextView) rootView.findViewById(R.id.me_grade_dist_key_a);
            textView.setTypeface(IuvoApplication.typeface);
            textView = (TextView) rootView.findViewById(R.id.me_grade_dist_key_b);
            textView.setTypeface(IuvoApplication.typeface);
            textView = (TextView) rootView.findViewById(R.id.me_grade_dist_key_c);
            textView.setTypeface(IuvoApplication.typeface);
            textView = (TextView) rootView.findViewById(R.id.me_grade_dist_key_d);
            textView.setTypeface(IuvoApplication.typeface);
            textView = (TextView) rootView.findViewById(R.id.me_grade_dist_key_f);
            textView.setTypeface(IuvoApplication.typeface);


            if (courseCountAttempted == 0) { // Show the gray bar only to indicate no grades.

                view = (View) rootView.findViewById(R.id.me_grade_dist_blank);
                params = (LayoutParams) view.getLayoutParams();
                params.weight = 1.0f;
                view.setLayoutParams(params);
                view.setBackgroundColor(getResources().getColor(R.color.gray));

                view = (View) rootView.findViewById(R.id.me_grade_dist_a);
                params = (LayoutParams) view.getLayoutParams();
                params.weight = 0.0f;
                textView = (TextView) rootView.findViewById(R.id.me_grade_dist_key_a);
                view.setLayoutParams(params);
                textView.setText(this.getString(R.string.me_grade_dist_key_a));

                view = (View) rootView.findViewById(R.id.me_grade_dist_b);
                params = (LayoutParams) view.getLayoutParams();
                params.weight = 0.0f;
                textView = (TextView) rootView.findViewById(R.id.me_grade_dist_key_b);
                view.setLayoutParams(params);
                textView.setText(this.getString(R.string.me_grade_dist_key_b));

                view = (View) rootView.findViewById(R.id.me_grade_dist_c);
                params = (LayoutParams) view.getLayoutParams();
                params.weight = 0.0f;
                textView = (TextView) rootView.findViewById(R.id.me_grade_dist_key_c);
                view.setLayoutParams(params);
                textView.setText(this.getString(R.string.me_grade_dist_key_c));

                view = (View) rootView.findViewById(R.id.me_grade_dist_d);
                params = (LayoutParams) view.getLayoutParams();
                params.weight = 0.0f;
                textView = (TextView) rootView.findViewById(R.id.me_grade_dist_key_d);
                view.setLayoutParams(params);
                textView.setText(this.getString(R.string.me_grade_dist_key_d));

                view = (View) rootView.findViewById(R.id.me_grade_dist_f);
                params = (LayoutParams) view.getLayoutParams();
                params.weight = 0.0f;
                textView = (TextView) rootView.findViewById(R.id.me_grade_dist_key_f);
                view.setLayoutParams(params);
                textView.setText(this.getString(R.string.me_grade_dist_key_f));

            } else {
                // Hide the gray "No grades" bar
                view = (View) rootView.findViewById(R.id.me_grade_dist_blank);
                params = (LayoutParams) view.getLayoutParams();
                params.weight = 0.0f;
                view.setLayoutParams(params);

                int gradeCount;
                String gradePercentageString;

                // A
                gradeCount = IuvoApplication.db.getACount();
                view = rootView.findViewById(R.id.me_grade_dist_a);
                view.setBackgroundColor(getResources().getColor(R.color.theme_green));
                ((LayoutParams) view.getLayoutParams()).weight = (float) gradeCount;
                gradePercentageString = format.format(((double) gradeCount / (double) courseCountCompleted) * 100);
                ((TextView) rootView.findViewById(R.id.me_grade_dist_key_a)).setText(this.getString(R.string.me_grade_dist_key_a, gradePercentageString));

                // B
                gradeCount = IuvoApplication.db.getBCount();
                view = rootView.findViewById(R.id.me_grade_dist_b);
                view.setBackgroundColor(getResources().getColor(R.color.theme_teal));
                ((LayoutParams) view.getLayoutParams()).weight = (float) gradeCount;
                gradePercentageString = format.format(((double) gradeCount / (double) courseCountCompleted) * 100);
                ((TextView) rootView.findViewById(R.id.me_grade_dist_key_b)).setText(this.getString(R.string.me_grade_dist_key_b, gradePercentageString));

                // C
                gradeCount = IuvoApplication.db.getCCount();
                view = rootView.findViewById(R.id.me_grade_dist_c);
                view.setBackgroundColor(getResources().getColor(R.color.theme_blue));
                ((LayoutParams) view.getLayoutParams()).weight = (float) gradeCount;
                gradePercentageString = format.format(((double) gradeCount / (double) courseCountCompleted) * 100);
                ((TextView) rootView.findViewById(R.id.me_grade_dist_key_c)).setText(this.getString(R.string.me_grade_dist_key_c, gradePercentageString));

                // D
                gradeCount = IuvoApplication.db.getDCount();
                view = rootView.findViewById(R.id.me_grade_dist_d);
                view.setBackgroundColor(getResources().getColor(R.color.theme_gray));
                ((LayoutParams) view.getLayoutParams()).weight = (float) gradeCount;
                gradePercentageString = format.format(((double) gradeCount / (double) courseCountCompleted) * 100);
                ((TextView) rootView.findViewById(R.id.me_grade_dist_key_d)).setText(this.getString(R.string.me_grade_dist_key_d, gradePercentageString));

                // F
                gradeCount = IuvoApplication.db.getFCount();
                view = rootView.findViewById(R.id.me_grade_dist_f);
                view.setBackgroundColor(getResources().getColor(R.color.android_dark_red));
                ((LayoutParams) view.getLayoutParams()).weight = (float) gradeCount;
                gradePercentageString = format.format(((double) gradeCount / (double) courseCountCompleted) * 100);
                ((TextView) rootView.findViewById(R.id.me_grade_dist_key_f)).setText(this.getString(R.string.me_grade_dist_key_f, gradePercentageString));

            }

            // Database
            int databaseSemesterCount = IuvoApplication.db.getSemesterCount();
            int databaseGroupCount = IuvoApplication.db.getGroupCount();
            int databaseCourseCount = IuvoApplication.db.getCourseCount();

            textView = (TextView) rootView.findViewById(R.id.me_database_semester_count);
            textView.setTypeface(IuvoApplication.typeface);
            textView.setText((CharSequence) String.valueOf(databaseSemesterCount));

            textView = (TextView) rootView.findViewById(R.id.me_database_semester_count_desc);
            if (databaseSemesterCount == 1)
                textView.setText((CharSequence) "Semester");
            else
                textView.setText((CharSequence) "Semesters");

            textView = (TextView) rootView.findViewById(R.id.me_database_group_count);
            textView.setTypeface(IuvoApplication.typeface);
            textView.setText((CharSequence) String.valueOf(databaseGroupCount));

            textView = (TextView) rootView.findViewById(R.id.me_database_group_count_desc);
            if (databaseGroupCount == 1)
                textView.setText((CharSequence) "Group");
            else
                textView.setText((CharSequence) "Groups");

            textView = (TextView) rootView.findViewById(R.id.me_database_course_count);
            textView.setTypeface(IuvoApplication.typeface);
            textView.setText((CharSequence) String.valueOf(databaseCourseCount));

            textView = (TextView) rootView.findViewById(R.id.me_database_course_count_desc);
            if (databaseCourseCount == 1)
                textView.setText((CharSequence) "Course");
            else
                textView.setText((CharSequence) "Courses");
        }
    }

    /**
     * Plan section fragment.
     */
    public static class PlanSectionFragment extends ListFragment {


        // Static variables.
        static final List<String> groupTitles = new ArrayList<String>();
        static final List<Integer> groupIDs = new ArrayList<Integer>();
        static final List<String> courseTitles = new ArrayList<String>();
        static final List<String> courseSemesters = new ArrayList<String>();
        static final List<String> courseSemesterColors = new ArrayList<String>();
        static final List<String> courseGrades = new ArrayList<String>();
        static final List<Integer> courseIDs = new ArrayList<Integer>();

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

            List<Group> groups = IuvoApplication.db.getAllGroups();

            // If user didn't create any groups, populate "Let's get started" dialogue into the headers and display.
            if (groups.size() == 0) {

                groupTitles.add(getString(R.string.plan_getting_started_header));

                listView = getListView();
                setListAdapter(new getStartedListAdapter(this.getContext()));

            } else { // User created groups, populate the appropriate headers, titles, etc and display them.

                for (Group g : groups) {

                    groupTitles.add(g.getName());
                    groupIDs.add(g.getID());
                    courseTitles.add(null);
                    courseSemesters.add(null);
                    courseSemesterColors.add(null);
                    courseGrades.add(null);
                    courseIDs.add(null);

                    if (IuvoApplication.db.getCourseCountByGroup(g.getID()) == 0) {
                        groupTitles.add(null);
                        groupIDs.add(null);
                        courseTitles.add(MAINACTIVITY_EMPTY_GROUP_KEY);
                        courseSemesters.add(null);
                        courseSemesterColors.add(null);
                        courseGrades.add(null);
                        courseIDs.add(null);
                    }

                    List<Course> courses = IuvoApplication.db.getAllCoursesByGroup(g.getID());
                    for (Course c : courses) {

                        groupTitles.add(null);
                        groupIDs.add(null);
                        courseTitles.add(c.getName() + " (" + c.getHours() + ")");

                        if (c.getSemesterID() == -1) {
                            courseSemesters.add(getString(R.string.plan_no_semester_assigned));
                            courseSemesterColors.add("Gray");
                        } else {
                            Semester semester = IuvoApplication.db.getSemester(c.getSemesterID());
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
                setListAdapter(new DegreePlanListAdapter(this.getContext()));
            }
        }
        // End of onResume

        // BaseAdapter explicitly designed to show the "Let's get started" dialogue
        private class getStartedListAdapter extends BaseAdapter {

            private final Context mContext;

            getStartedListAdapter (Context mContext) {
                this.mContext = mContext;

            }

            @Override
            public int getCount() {
                return groupTitles.size();
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            // Disables the highlight selection that appears when selecting a list item.
            @Override
            public boolean isEnabled(int position) {
                return false;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View rootView = convertView;

                rootView = LayoutInflater.from(mContext).inflate(
                        R.layout.fragment_plan_list_tutorial, parent, false);
                rootView.setId(R.id.list_tutorial);

                TextView headerTextView = (TextView) rootView.findViewById(R.id.plan_getting_started_header);
                headerTextView.setTypeface(IuvoApplication.typeface);
                headerTextView = (TextView) rootView.findViewById(R.id.plan_getting_started_semester_title);
                headerTextView.setTypeface(IuvoApplication.typeface);
                headerTextView = (TextView) rootView.findViewById(R.id.plan_getting_started_group_title);
                headerTextView.setTypeface(IuvoApplication.typeface);
                headerTextView = (TextView) rootView.findViewById(R.id.plan_getting_started_course_grade0);
                headerTextView.setTypeface(IuvoApplication.typeface);
                headerTextView = (TextView) rootView.findViewById(R.id.plan_getting_sample_group1);
                headerTextView.setTypeface(IuvoApplication.typeface);
                headerTextView = (TextView) rootView.findViewById(R.id.plan_getting_started_course_grade1);
                headerTextView.setTypeface(IuvoApplication.typeface);
                headerTextView = (TextView) rootView.findViewById(R.id.plan_getting_started_course_grade2);
                headerTextView.setTypeface(IuvoApplication.typeface);
                headerTextView = (TextView) rootView.findViewById(R.id.plan_getting_sample_group2);
                headerTextView.setTypeface(IuvoApplication.typeface);
                headerTextView = (TextView) rootView.findViewById(R.id.plan_getting_started_course_grade3);
                headerTextView.setTypeface(IuvoApplication.typeface);

                return rootView;

            }
        } // End of getStartedListAdapter

        // Plan section fragment. Designed to show the degree plan as a list with headers.
        private class DegreePlanListAdapter extends BaseAdapter {

            private final Context mContext;

            public DegreePlanListAdapter(Context mContext) {
                this.mContext = mContext;

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
                String emptyText = courseTitles.get(position);
                View rootView = convertView;

                if (headerText != null) { // Group Header

                    if (convertView == null || convertView.getId() == R.id.list_item || convertView.getId() == R.id.list_empty) {
                        rootView = LayoutInflater.from(mContext).inflate(
                                R.layout.fragment_plan_list_header, parent, false);
                        rootView.setId(R.id.list_header);
                    }

                    TextView headerTextView = (TextView) rootView.findViewById(R.id.header_name_textview);


                    headerTextView.setTypeface(IuvoApplication.typeface);
                    headerTextView.setText(headerText);

                    rootView.setTag(groupIDs.get(position));

                    rootView.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            int groupID = (Integer) view.getTag();

                            if (IuvoApplication.db.getCourseCountByGroup(groupID) == 0) {
                                Toast.makeText(mContext, view.getResources().getString(R.string.plan_no_courses_found), Toast.LENGTH_LONG).show();
                            } else {
                                Intent intent = new Intent(mContext, CoursesActivity.class);
                                intent.putExtra(MAINACTIVITY_GROUP_ID, groupID);
                                startActivity(intent);
                            }
                        }

                    });

                    return rootView;

                } else if (emptyText != null && emptyText.equals(MAINACTIVITY_EMPTY_GROUP_KEY)) { // Empty group indicator

                    if (convertView == null || convertView.getId() == R.id.list_item || convertView.getId() == R.id.list_header) {
                        rootView = LayoutInflater.from(mContext).inflate(
                                R.layout.fragment_plan_list_empty, parent, false);
                        rootView.setId(R.id.list_empty);
                    }
                    return rootView;

                } else { // Course Item

                    if (convertView == null || convertView.getId() == R.id.list_header || convertView.getId() == R.id.list_empty) {
                        rootView = LayoutInflater.from(mContext).inflate(
                                R.layout.fragment_plan_list_item, parent, false);
                        rootView.setId(R.id.list_item);
                    }

                    TextView header = (TextView) rootView.findViewById(R.id.plan_item_title);
                    header.setText(courseTitles.get(position));

                    TextView subtext = (TextView) rootView.findViewById(R.id.plan_item_description);
                    subtext.setText(courseSemesters.get(position));

                    rootView.setBackgroundColor(ColorHandler.getColor(mContext, courseSemesterColors.get(position)));

                    TextView gradeText = (TextView) rootView.findViewById(R.id.plan_item_grade);

                    gradeText.setTypeface(IuvoApplication.typeface);
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
        } // End of DegreePlanListAdapter
    }

    /**
     * More section fragment.
     */
    public static class MoreSectionFragment extends ListFragment {

        private ListView listView;

        public static final List<String> moreTitles = new ArrayList<String>();
        public static final List<String> moreSubtitles = new ArrayList<String>();

        private static final String LIST_TITLE = "LIST_TITLE";

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

                for (int i = 0; i < titles.length; i++) {
                    moreTitles.add(titles[i]);
                    moreSubtitles.add(subTitles[i]);
                }
            }
            listView = getListView();
            setListAdapter(new moreListAdapter(this.getContext()));
        }

        // BaseAdapter that shows the list seen in the 'more' tab.
        private class moreListAdapter extends BaseAdapter {

            private final Context mContext;

            public moreListAdapter(Context mContext) {
                this.mContext = mContext;

            }

            @Override
            public int getCount() {
                return moreTitles.size();
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

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

                TextView headerTextView = (TextView) rootView.findViewById(R.id.more_item_title);
                headerTextView.setText(headerTitle);
                headerTextView.setTypeface(IuvoApplication.typeface);

                headerTextView = (TextView) rootView.findViewById(R.id.more_item_subtitle);
                headerTextView.setText(headerSubtitle);

                switch (position) {
                    case 1:
                        rootView.setBackgroundColor(getResources().getColor(R.color.theme_blue));
                        break;
                    case 2:
                        rootView.setBackgroundColor(getResources().getColor(R.color.theme_teal));
                        break;
                    case 3:
                        rootView.setBackgroundColor(getResources().getColor(R.color.theme_green));
                        break;
                    case 4:
                        rootView.setBackgroundColor(getResources().getColor(R.color.gray));
                        break;
                }

                rootView.setId(position);

                rootView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        int id = view.getId();
                        Intent intent;

                        switch (id) {
                            case 0: // View hidden courses

                                if (IuvoApplication.db.getCourseCountByGroup(-1) == 0) {
                                    Toast.makeText(mContext, view.getResources().getString(R.string.plan_no_courses_found), Toast.LENGTH_LONG).show();
                                } else {
                                    intent = new Intent(mContext, CoursesActivity.class);
                                    intent.putExtra(MAINACTIVITY_GROUP_ID, -1);
                                    startActivity(intent);
                                }
                                break;
                            case 1: // GPA Scale
                                intent = new Intent(mContext, PointsActivity.class);
                                startActivity(intent);
                                break;
                            case 2: // Backup/Restore
                                intent = new Intent(mContext, BackupActivity.class);
                                startActivity(intent);
                                break;
                            case 3: // Tutorial
                                intent = new Intent(mContext, StartHelpActivity.class);
                                startActivity(intent);
                                break;
                            case 4: // About
                                intent = new Intent(mContext, AboutActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                });

                return rootView;
            }
        }
    }

    /**
     * SCRAPPED. Pops up with a dialog so that user can edit their profile.
     public void menuEditProfile() {
     Dialogs.editProfile(this, this.getLayoutInflater(), IuvoApplication.settings).show();
     }
     */

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

        SharedPreferences.Editor iuvoSettingsEditor = IuvoApplication.settings.edit();
        iuvoSettingsEditor.putString("name", name);
        iuvoSettingsEditor.putString("school", school);
        iuvoSettingsEditor.putString("major", major);
        if (!classification.equals("Select Classification"))
            iuvoSettingsEditor.putString("classification", classification);
        else
            iuvoSettingsEditor.putString("classification", "");
        iuvoSettingsEditor.apply();
    }
}