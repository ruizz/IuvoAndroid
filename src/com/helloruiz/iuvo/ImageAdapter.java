package com.helloruiz.iuvo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
        ImageView imageView;
        TextView textView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	rootView = li.inflate(R.layout.fragment_more_grid_item, null);
        	
            imageView = (ImageView) rootView.findViewById(R.id.more_item_image);
            imageView.setImageResource(mImageIds[position]);
            
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 50);
            params.height = 250;
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            
            textView = (TextView) rootView.findViewById(R.id.more_item_title);
            textView.setText(mTitleIds[position]);
            
            textView = (TextView) rootView.findViewById(R.id.more_item_description);
            textView.setText(mDescriptionIds[position]);
        } else {
            rootView = convertView;
        }

        return rootView;
    }

    // references to our images
    private Integer[] mImageIds = {
            R.drawable.more_drawable_01, R.drawable.more_drawable_02,
            R.drawable.more_drawable_03
    };
    
    private CharSequence[] mTitleIds = {
    	"About", "Source", "Visit"	
    };
    
    private CharSequence[] mDescriptionIds = {
        "Iuvo information and credits", "View the source code on GitHub", "Check out my website, helloruiz.com"	
    };
    
}