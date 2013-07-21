package com.helloruiz.iuvo;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.helloruiz.iuvo.database.Group;
import com.helloruiz.iuvo.database.IuvoApplication;
import com.helloruiz.iuvo.help.GroupsHelpActivity;
import com.mobeta.android.dslv.DragSortListView;

public class GroupsActivity extends ListActivity {
	
	/**
	 * -- Classes --
	 */
	private class GroupAdapter extends ArrayAdapter<Group> {
	      
	      public GroupAdapter(List<Group> groups) {
	        super(GroupsActivity.this, R.layout.activity_groups_list_item,
	          R.id.group_name_textview, groups);
	      }

	      public View getView(int position, View convertView, ViewGroup parent) {
	        View v = super.getView(position, convertView, parent);
	        
	        String courseCount = String.valueOf(IuvoApplication.db.getCourseCountByGroupPosition(position));
	        
	        if (courseCount.equals("1"))
	        	courseCount = courseCount + SINGLE_COURSE;
	        else
	        	courseCount = courseCount + MULTIPLE_COURSES;
	        
	        String groupGPA = String.valueOf(IuvoApplication.db.getGPAByGroupPosition(position));
	        groupGPA = ", " + groupGPA + " GPA";

	        TextView textView = (TextView) v.findViewById(R.id.group_name_textview);
            textView.setTypeface(IuvoApplication.typeface);
            
            textView = (TextView) v.findViewById(R.id.group_class_count_textview);
            textView.setText((CharSequence) (courseCount + groupGPA));
	        
            // Background color would change after messing with semesters. Band-Aid fix.
            v.setBackgroundColor(getResources().getColor(R.color.theme_blue));
            
	        return v;
	      }
	    }
	
	/**
	 * -- Variables --
	 */
	private GroupAdapter groupAdapter;

    private ArrayList<Group> mGroups;
    
    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
                
    	@Override
    	public void drop(int from, int to) {

			IuvoApplication.db.moveGroup(from, to);     
    		
			refreshListAdapter();
			getListView().setSelection(to - 2);

    	}
    };
	
	// Whenever a user swipes an group item left or right to delete.
    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
		
		@Override
		public void remove(int which) {
			deleteGroupConfirm(which);			
		}
	};
	
	// Strings defined globally here since they'll be used in a loop.
	String SINGLE_COURSE = " Course";
	String MULTIPLE_COURSES = " Courses";
	
	/**
	 * -- Overrides --
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups);
		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Set up our drag sort ListView
		DragSortListView dragSortListView = (DragSortListView) getListView();
		dragSortListView.setDropListener(onDrop);
		dragSortListView.setRemoveListener(onRemove);
		
		refreshListAdapter();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.groups, menu);
		return true;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Group item = groupAdapter.getItem(position);
		Dialogs.editGroup(this, item);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.menu_group_help:
			menuGroupsHelp();
			return true;
		case R.id.menu_group_add:
			menuAddGroup();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * -- Voids --
	 */
	// Pops up with dialog so that user get help
    public void menuGroupsHelp() {
    	Intent intent = new Intent(getApplicationContext(), GroupsHelpActivity.class);
    	startActivity(intent);
    }
	
	// Pops up with dialog so that user can add a group
    public void menuAddGroup() {
    	Dialogs.addGroup(this);
    }
    
    // Pops up with dialog so that user can confirm deletion
    public void deleteGroupConfirm(int which) {
    	Dialogs.deleteGroupConfirm(this, which);
    }

    // Executes after user adds a group from addMenuGroup
	public void addGroup(String name) {
		IuvoApplication.db.addGroup(name);
		refreshListAdapter();
	}
	
	// Called after a user has submitted an update for a group
	public void editGroup(String newName, Group group) {
		group.setName(newName);
		IuvoApplication.db.updateGroup(group);
		
		refreshListAdapter();
	}
	
	// Executes after user has confirmed that they want to delete a group
	public void deleteGroup(int which) {
		Group item = IuvoApplication.db.getGroupByPosition(which);
		
		// Remove group from database
		IuvoApplication.db.deleteGroup(item);		
		refreshListAdapter();
	}	
	
	// Called whenever the Drag Sort ListView needs to be updated to reflect database changes
	public void refreshListAdapter() {
		// Refresh the ListAdapter to reflect the new changes in the database.
		List<Group> groupsInDatabase = IuvoApplication.db.getAllGroups();
		
		Log.d("Group: ", "Updating ListAdapter...");
		mGroups = new ArrayList<Group>();
		for (Group g : groupsInDatabase) {
			Log.d("Group: ", "Position: " + g.getPosition() + ", ID: " + g.getID() + ", Name: " + g.getName());
			mGroups.add(g);
		}
		
		groupAdapter = new GroupAdapter(mGroups);
		setListAdapter(groupAdapter);
	}
}
