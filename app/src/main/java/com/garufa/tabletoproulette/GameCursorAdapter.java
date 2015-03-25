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
        TextView  textView  = (TextView)  view.findViewById(R.id.adapter_textView_title);
        String name = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_GAME_NAME));

        String file_name = cursor.getString( cursor.getColumnIndexOrThrow(Constants.COLUMN_BGG_ID));
        imageView.setImageBitmap(get_thumbnail(context, file_name));

        textView.setText(name);
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
