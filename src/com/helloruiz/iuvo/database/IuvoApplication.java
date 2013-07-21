package com.helloruiz.iuvo.database;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class IuvoApplication extends Application {
	
	/**
	 * Variables
	 */
	// DatabaseHandler for entire application.
	public static DatabaseHandler db;
	
	// Typeface for pretty lobster font
	public static Typeface typeface;
	
	// Application preferences
    public static SharedPreferences settings;
    
    // GPA points scale
    public static float pointsAPlus;
    public static float pointsA;
    public static float pointsAMinus;
    public static float pointsBPlus;
    public static float pointsB;
    public static float pointsBMinus;
    public static float pointsCPlus;
    public static float pointsC;
    public static float pointsCMinus;
    public static float pointsDPlus;
    public static float pointsD;
    public static float pointsDMinus;
	
	/**
	 * Overrides
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		
		// Define everything
		db = new DatabaseHandler(this);
		typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lobster.otf");
        settings = getSharedPreferences("User", 0);
        updatePoints();
	}
	
	/**
	 * Voids
	 */
	public static void hideKeyboard(Context context, EditText e) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(e.getWindowToken(), 0);
	}
	
	public static void updatePoints() {
		pointsAPlus = settings.getFloat("pointsAPlus", (float) 4.33);
		pointsA = settings.getFloat("pointsA", (float) 4.00);
		pointsAMinus = settings.getFloat("pointsAMinus", (float) 3.67);
		
		pointsBPlus = settings.getFloat("pointsBPlus", (float) 3.33);
		pointsB = settings.getFloat("pointsB", (float) 3.00);
		pointsBMinus = settings.getFloat("pointsBMinus", (float) 2.67);
		
		pointsCPlus = settings.getFloat("pointsCPlus", (float) 2.33);
		pointsC = settings.getFloat("pointsC", (float) 2.00);
		pointsCMinus = settings.getFloat("pointsCMinus", (float) 1.67);
		
		pointsDPlus = settings.getFloat("pointsDPlus", (float) 1.33);
		pointsD = settings.getFloat("pointsD", (float) 1.00);
		pointsDMinus = settings.getFloat("pointsDMinus", (float) 0.67);
	}
}
