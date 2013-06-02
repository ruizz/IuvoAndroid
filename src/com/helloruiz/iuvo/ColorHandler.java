package com.helloruiz.iuvo;

import android.graphics.Color;

/**
 * For handling colors. Give it a string such as "Sweet Pea Green", and it'll turn the color value.
 * @author Ruiz
 *
 */
public class ColorHandler {

	public static int getColor(SemestersActivity semestersActivity, String colorName) {
		
		if (colorName.equals("Android Dark Blue"))
			return semestersActivity.getResources().getColor(R.color.android_dark_blue);
		
		if (colorName.equals("Android Dark Purple"))
			return semestersActivity.getResources().getColor(R.color.android_dark_purple);
		
		if (colorName.equals("Android Dark Green"))
			return semestersActivity.getResources().getColor(R.color.android_dark_green);
		
		if (colorName.equals("Android Dark Yellow"))
			return semestersActivity.getResources().getColor(R.color.android_dark_yellow);
		
		if (colorName.equals("Android Dark Red"))
			return semestersActivity.getResources().getColor(R.color.android_dark_red);
		
		if (colorName.equals("Plum"))
			return semestersActivity.getResources().getColor(R.color.plum);
		
		if (colorName.equals("Wild Berry"))
			return semestersActivity.getResources().getColor(R.color.wild_berry);
		
		if (colorName.equals("Purple"))
			return semestersActivity.getResources().getColor(R.color.purple);
		
		if (colorName.equals("Crimson"))
			return semestersActivity.getResources().getColor(R.color.crimson);
		
		if (colorName.equals("Maroon"))
			return semestersActivity.getResources().getColor(R.color.maroon);
		
		if (colorName.equals("Rust"))
			return semestersActivity.getResources().getColor(R.color.rust);
		
		if (colorName.equals("Brick"))
			return semestersActivity.getResources().getColor(R.color.brick);
		
		if (colorName.equals("Watermelon"))
			return semestersActivity.getResources().getColor(R.color.watermelon);
		
		if (colorName.equals("Goldfish"))
			return semestersActivity.getResources().getColor(R.color.goldfish);
		
		if (colorName.equals("Construction Zone"))
			return semestersActivity.getResources().getColor(R.color.construction_zone);
		
		if (colorName.equals("Mustard"))
			return semestersActivity.getResources().getColor(R.color.mustard);
		
		if (colorName.equals("Forest"))
			return semestersActivity.getResources().getColor(R.color.forest);
		
		if (colorName.equals("Sea Green"))
			return semestersActivity.getResources().getColor(R.color.sea_green);
		
		if (colorName.equals("Split Pea Soup"))
			return semestersActivity.getResources().getColor(R.color.split_pea_soup);
		
		if (colorName.equals("Peru"))
			return semestersActivity.getResources().getColor(R.color.peru);
		
		if (colorName.equals("Milk Chocolate"))
			return semestersActivity.getResources().getColor(R.color.milk_chocolate);
		
		if (colorName.equals("Dark Chocolate"))
			return semestersActivity.getResources().getColor(R.color.dark_chocolate);

		return semestersActivity.getResources().getColor(R.color.gray);
	}
}
