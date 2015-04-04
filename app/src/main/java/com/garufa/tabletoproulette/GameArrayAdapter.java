package com.garufa.tabletoproulette;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 3/14/2015.
 */
public class GameArrayAdapter extends ArrayAdapter<Game> {
    public GameArrayAdapter(Context context, List<Game> games) {
        super(context, 0, games);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Game game = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_game_no_image,
                    parent, false);
        }

        // Lookup view for data population
        TextView textView_name = (TextView) convertView.findViewById(R.id.textView_item_name);
        TextView textView_year = (TextView) convertView.findViewById(R.id.item_year_TextView);

        // Populate the data into the template view using the data object
        textView_name.setText(game.get_name());
        String year = String.valueOf(game.get_year());
        if (year.equals("0")) {
            textView_year.setVisibility(View.GONE);
        } else {
            textView_year.setText(year);
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
