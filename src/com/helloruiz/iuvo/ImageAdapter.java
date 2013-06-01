package com.helloruiz.iuvo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ImageAdapter for the gridView on the 'more' tab.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mImageIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
    	View rootView;
        //ImageView imageView;
        TextView textView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	rootView = li.inflate(R.layout.fragment_more_grid_item, null);
        	
        	// No longer needed, since we're not using bitmaps for the more grid items anymore.
        	// GridView item has been changed from RelativeLayout to LinearLayout anyways.
            //imageView = (ImageView) rootView.findViewById(R.id.more_item_image);
            //imageView.setImageResource(mImageIds[position]);
            
            //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
            //params.height = 150;
            //imageView.setLayoutParams(params);
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            
            textView = (TextView) rootView.findViewById(R.id.more_item_title);
            textView.setText(mTitleIds[position]);
            textView.setBackgroundColor(Color.parseColor(mContext.getString(mImageIds[position])));
            Typeface typeFace=Typeface.createFromAsset(rootView.getContext().getAssets(),"fonts/lobster.otf");
            textView.setTypeface(typeFace);
            
            textView = (TextView) rootView.findViewById(R.id.more_item_description);
            textView.setText(mDescriptionIds[position]);
            textView.setBackgroundColor(Color.parseColor(mContext.getString(mImageIds[position])));
            
        } else {
            rootView = convertView;
        }

        return rootView;
    }

    // references to our images
    private Integer[] mImageIds = {
            R.color.theme_gray, R.color.theme_blue,
            R.color.theme_teal
    };
    
    private CharSequence[] mTitleIds = {
    	"About", "Source", "Visit"	
    };
    
    private CharSequence[] mDescriptionIds = {
        "Iuvo information and credits", "View the source code on GitHub", "Check out my website, helloruiz.com"	
    };
    
}