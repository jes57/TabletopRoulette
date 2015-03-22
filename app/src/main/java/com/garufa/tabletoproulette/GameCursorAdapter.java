package com.garufa.tabletoproulette;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        TextView  textView  = (TextView)  view.findViewById(R.id.adapter_textView_title);
        String game = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_GAME_NAME));

        String url = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_IMAGE_URL));
//        new ImageLoadTask(url, imageView).execute();
        textView.setText(game);
    }
}
