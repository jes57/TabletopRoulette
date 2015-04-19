package com.garufa.tabletoproulette;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Jason on 3/7/2015.
 */
public class GameCursorAdapter extends CursorAdapter {

    public GameCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.adapter_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView) view.findViewById(R.id.adapterItem_image);
        TextView  textView_title  = (TextView)  view.findViewById(R.id.adapterItem_name);
        TextView  textView_year = (TextView) view.findViewById(R.id.adapterItem_year);
        TextView  textView_players  = (TextView)  view.findViewById(R.id.adapterItem_players);
        TextView  textView_time  = (TextView)  view.findViewById(R.id.adapterItem_play_time);
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.adapterItem_ratingBar);

        // Get the data
        String name = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_GAME_NAME));

        String file_name = cursor.getString( cursor.getColumnIndexOrThrow(Constants.COLUMN_BGG_ID));
        imageView.setImageBitmap(get_thumbnail(context, file_name));

        textView_title.setText(name);
        textView_players.setText(get_players(cursor));
        textView_time.setText(get_time(cursor));
        ratingBar.setRating(Float.parseFloat(get_rating(cursor)));
        textView_year.setText(get_year(cursor));
        textView_year.setVisibility(View.GONE);
    }

    private String get_rating(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_RATING));
    }
    private String get_year(Cursor cursor){
        return cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_YEAR));
    }

    private String get_time(Cursor cursor) {
        String play_time_min, play_time_max, play_time;
        play_time_min = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_MIN_PLAY_TIME));
        play_time_max = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_MAX_PLAY_TIME));

        if (play_time_max.equals(play_time_min)) {
            play_time = play_time_min;
        } else {
            play_time = play_time_min + "-" + play_time_max;
        }
        return play_time;
    }

    private String get_players(Cursor cursor) {
        String players_min, players_max, players;
        players_min = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_MIN_PLAYERS));
        players_max = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_MAX_PLAYERS));

        if (players_max.equals(players_min)) {
            players = players_min;
        } else {
            players = players_min + "-" + players_max;
        }
        return players;
    }

    public Bitmap get_thumbnail(Context context, String game_id) {
        String file_name = game_id + Constants.FILE_TYPE;

        try {
            File file_path = context.getFileStreamPath(file_name);
            FileInputStream inputStream = new FileInputStream(file_path);
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.tabletoproulet);
        }
    }
}
