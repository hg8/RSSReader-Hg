package com.epsi.hugo.rssreaderhg2.pck_classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.epsi.hugo.rssreaderhg2.R;

import java.util.ArrayList;


/**
 * Created by hugo on 07/04/15.
 */
public class PostAdapter extends ArrayAdapter<PostData> {
    public PostAdapter(Context context, ArrayList<PostData> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertedView, ViewGroup parentView)
    {
        //Defining the adapter
        //https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
        PostData postData = getItem(position);

        if(convertedView == null)
        {
            //inflate the view if the view is not used before
            convertedView = LayoutInflater.from(getContext()).inflate(R.layout.item, parentView, false);
        }

        //get title & description from views
        TextView title = (TextView) convertedView.findViewById(R.id.title);
        TextView description = (TextView) convertedView.findViewById(R.id.description);

        //insert inside the view infos from object
        title.setText(cutString(postData.getPostTitle()));
        description.setText(cutString(postData.getPostDescripion()));

        //return the new converted view
        return convertedView;

    }


    /* CREDIT : THOMAS MAIRE B3 */
    public String cutString(String chaine){
        if(chaine.length() >= 40){
            chaine = chaine.substring(0, 40);
            return chaine.concat("...");
        }
        return chaine;
    }
}
