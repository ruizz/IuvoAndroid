package com.helloruiz.iuvo;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.helloruiz.iuvo.database.DatabaseHandler;
import com.helloruiz.iuvo.database.Group;
import com.mobeta.android.dslv.DragSortListView;

public class GroupsActivity extends ListActivity {
	
	private GroupAdapter groupAdapter;

    private ArrayList<Group> mGroups;
    
    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
                
    	@Override
    	public void drop(int from, int to) {
    		
    		Group item = groupAdapter.getItem(from);
       
    		groupAdapter.remove(item);       
    		groupAdapter.insert(item, to);
    	}
    };
    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
		
		@Override
		public void remove(int which) {
			
			Group item = groupAdapter.getItem(which);
			
			// Remove group from database...
			DatabaseHandler databaseHandler = new DatabaseHandler(GroupsActivity.this);
			databaseHandler.deleteGroup(item);
			
			// And from the list adapter
			// Also decrements the IDs of the groups below what was deleted, referenced in groupAdapter.
			groupAdapter.remove(item);
			int size = groupAdapter.getCount();
			for (int i = which; i < size; i++) {
				item = groupAdapter.getItem(which);
				groupAdapter.remove(item);
				item.setID(item.getID() - 1);
				groupAdapter.add(item);
			}
		}
	};

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
		
		// Set up the database handler and get all of the groups in the database.
		DatabaseHandler databaseHandler = new DatabaseHandler(this);
		List<Group> groupsInDatabase = databaseHandler.getAllGroups();
		
		mGroups = new ArrayList<Group>();
		for (Group g : groupsInDatabase) {
			Log.d("Group: ", "ID: " + g.getID() + ", Name: " + g.getName());
			mGroups.add(g);
		}
		
		groupAdapter = new GroupAdapter(mGroups);
		
		setListAdapter(groupAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.groups, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.menu_group_add:
			menuAddGroup();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
     * Pops up with a dialog so that user can edit their profile.
     */
    public void menuAddGroup() {
    	DialogDatabase.addGroup(this);
    }

	private class GroupAdapter extends ArrayAdapter<Group> {
	      
	      public GroupAdapter(List<Group> groups) {
	        super(GroupsActivity.this, R.layout.activity_groups_list_item,
	          R.id.group_name_textview, groups);
	      }

	      public View getView(int position, View convertView, ViewGroup parent) {
	        View v = super.getView(position, convertView, parent);

	        return v;
	      }
	    }
	
	public void addGroup(String name) {
		
		DatabaseHandler databaseHandler = new DatabaseHandler(this);

		// This should be correct, since new max id is current number of available groups.
		int max = databaseHandler.getGroupCount();
		System.out.println("Before add, max: " + max);
		databaseHandler.addGroup(new Group(max, name));
		
		List<Group> groupsInDatabase = databaseHandler.getAllGroups();
		
		mGroups = new ArrayList<Group>();
		for (Group g : groupsInDatabase) {
			Log.d("Group: ", "ID: " + g.getID() + ", Name: " + g.getName());
			mGroups.add(g);
		}
		
		groupAdapter = new GroupAdapter(mGroups);
		
		setListAdapter(groupAdapter);
	}
}
