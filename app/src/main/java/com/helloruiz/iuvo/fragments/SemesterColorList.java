package com.helloruiz.iuvo.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.helloruiz.iuvo.R;

public class SemesterColorList extends ListFragment{

	/**
	 * Variables
	 */
	private HeadlineSelection headlineSelection;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Devices older than Honeycomb use 'simple_list_item_1'
		int layout = android.R.layout.simple_list_item_activated_1;

		String[] colorArray = getResources().getStringArray(R.array.color_array); 
		
		setListAdapter(new ArrayAdapter<String>(getActivity(), layout, colorArray));
	}
	
	/**
	 * Overrides
	 */
	@Override
	public void onStart() {
		super.onStart();

		// Keeps the selected item in the list highlighted.
		// According to the Open Source Project, it's done at onStart() because listview isn't available during onCreate().
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		getListView().setVerticalScrollBarEnabled(true);
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof Activity){
			// Tying the interface object with the host activity
			// Make sure that the host activity implements the interface!
			headlineSelection = (HeadlineSelection) context;
		}

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		headlineSelection.colorSelected(position);

		// "Set the item as checked to be highlighted when in two-pane layout"
		// This line didn't make any difference when I was testing it...
		getListView().setItemChecked(position, false);
	}

	/**
	 * Interfaces
	 */
	public interface HeadlineSelection {
		void colorSelected(int position);
	}

	
}