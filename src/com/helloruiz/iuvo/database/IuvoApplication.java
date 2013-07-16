package com.helloruiz.iuvo.database;

import android.app.Application;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class IuvoApplication extends Application {
	
	public static DatabaseHandler db;
	
	@Override
	public void onCreate() {
		super.onCreate();
		db = new DatabaseHandler(this);
	}
	
	public static void hideKeyboard(Context context, EditText e) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(e.getWindowToken(), 0);
	}
}
