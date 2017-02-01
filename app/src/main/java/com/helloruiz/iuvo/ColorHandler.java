package com.helloruiz.iuvo;

import android.content.Context;
import android.support.v4.content.ContextCompat;

/**
 * For handling colors. Give it a string such as "Sweet Pea Green", and it'll turn the color value.
 *
 */
class ColorHandler {

	public static int getColor(Context context, String colorName) {

		if (colorName.equals("Android Dark Blue"))
			return ContextCompat.getColor(context, R.color.android_dark_blue);

		if (colorName.equals("Android Dark Purple"))
			return ContextCompat.getColor(context, R.color.android_dark_purple);

		if (colorName.equals("Android Dark Green"))
			return ContextCompat.getColor(context, R.color.android_dark_green);

		if (colorName.equals("Android Dark Yellow"))
			return ContextCompat.getColor(context, R.color.android_dark_yellow);

		if (colorName.equals("Android Dark Red"))
			return ContextCompat.getColor(context, R.color.android_dark_red);

		if (colorName.equals("Plum"))
			return ContextCompat.getColor(context, R.color.plum);

		if (colorName.equals("Wild Berry"))
			return ContextCompat.getColor(context, R.color.wild_berry);

		if (colorName.equals("Grape"))
			return ContextCompat.getColor(context, R.color.grape);

		if (colorName.equals("Crimson"))
			return ContextCompat.getColor(context, R.color.crimson);

		if (colorName.equals("Maroon"))
			return ContextCompat.getColor(context, R.color.maroon);

		if (colorName.equals("Rust"))
			return ContextCompat.getColor(context, R.color.rust);

		if (colorName.equals("Brick"))
			return ContextCompat.getColor(context, R.color.brick);

		if (colorName.equals("Watermelon"))
			return ContextCompat.getColor(context, R.color.watermelon);

		if (colorName.equals("Goldfish"))
			return ContextCompat.getColor(context, R.color.goldfish);

		if (colorName.equals("Construction Zone"))
			return ContextCompat.getColor(context, R.color.construction_zone);

		if (colorName.equals("Mustard"))
			return ContextCompat.getColor(context, R.color.mustard);

		if (colorName.equals("Forest"))
			return ContextCompat.getColor(context, R.color.forest);

		if (colorName.equals("Sea Green"))
			return ContextCompat.getColor(context, R.color.sea_green);

		if (colorName.equals("Split Pea Soup"))
			return ContextCompat.getColor(context, R.color.split_pea_soup);

		if (colorName.equals("Peru"))
			return ContextCompat.getColor(context, R.color.peru);

		if (colorName.equals("Milk Chocolate"))
			return ContextCompat.getColor(context, R.color.milk_chocolate);

		if (colorName.equals("Dark Chocolate"))
			return ContextCompat.getColor(context, R.color.dark_chocolate);

		if (colorName.equals("Periwinkle"))
			return ContextCompat.getColor(context, R.color.periwinkle);

		if (colorName.equals("Navy"))
			return ContextCompat.getColor(context, R.color.theme_navy);

		if (colorName.equals("Cute Blue"))
			return ContextCompat.getColor(context, R.color.cute_blue);

		if (colorName.equals("Gold"))
			return ContextCompat.getColor(context, R.color.gold);

		if (colorName.equals("Jeans"))
			return ContextCompat.getColor(context, R.color.jeans);

		if (colorName.equals("Cool Blue"))
			return ContextCompat.getColor(context, R.color.cool_blue);

		if (colorName.equals("Orange"))
			return ContextCompat.getColor(context, R.color.orange);

		if (colorName.equals("Chaminade"))
			return ContextCompat.getColor(context, R.color.chaminade);

		if (colorName.equals("Barbie"))
			return ContextCompat.getColor(context, R.color.barbie);

		if (colorName.equals("Gray"))
			return ContextCompat.getColor(context, R.color.gray);

		return ContextCompat.getColor(context, R.color.gray);
	}
}
