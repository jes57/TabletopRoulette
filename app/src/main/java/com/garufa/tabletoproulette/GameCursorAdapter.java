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
        ImageView imageView = (ImageView) view.findViewById(R.id.adapter_imageView);
        TextView  textView_title  = (TextView)  view.findViewById(R.id.adapter_textView_title);
        TextView  textView_players  = (TextView)  view.findViewById(R.id.adapter_textView_players);
        TextView  textView_time  = (TextView)  view.findViewById(R.id.adapter_textView_play_time);
        TextView  textView_rating  = (TextView)  view.findViewById(R.id.adapter_textView_rating);

        // Get the data
        String name = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_GAME_NAME));
        String players = get_players(cursor);
        String time = get_time(cursor);
        String rating = get_rating(cursor);

        String file_name = cursor.getString( cursor.getColumnIndexOrThrow(Constants.COLUMN_BGG_ID));
        imageView.setImageBitmap(get_thumbnail(context, file_name));

        textView_title.setText(name);
        textView_players.setText(players);
        textView_time.setText(time);
        textView_rating.setText(rating);
    }

    private String get_rating(Cursor cursor) {
        String rating = "Rating: " +
                cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_RATING));
        return rating;
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
            e.printStackTrace();
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.tabletoproulet);
        }
    }
}
