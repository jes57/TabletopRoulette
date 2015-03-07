package com.garufa.tabletoproulette;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 3/7/2015.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "collectionDB.db";
    private static final String TABLE_GAMES   = "games";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_GAME_NAME = "game_name";
    public static final String COLUMN_DESCRIPTION = "description";

    public DBHandler(Context context, String name,
                     SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GAMES_TABLE = "CREATE TABLE "
                + TABLE_GAMES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_GAME_NAME
                + " TEXT," + COLUMN_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_GAMES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
        onCreate(db);
    }

    public void addGame(Game game){
        ContentValues values = new ContentValues();
        values.put(COLUMN_GAME_NAME, game.get_name());
        values.put(COLUMN_DESCRIPTION, game.get_description());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_GAMES, null, values);
        db.close();
    }

    public Game findGame(String game_name){
        String query = "Select * FROM " + TABLE_GAMES + " WHERE " + COLUMN_GAME_NAME
                + " =  \"" + game_name + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Game game = new Game();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            game.set_id(Integer.parseInt(cursor.getString(0)));
            game.set_name(cursor.getString(1));
            game.set_description(cursor.getString(2));
            cursor.close();
        } else {
            game = null;
        }
        db.close();
        return game;

    }

    public boolean deleteGame(String game_name){
        boolean result = false;

        String query = "Select * FROM " + TABLE_GAMES + " WHERE " + COLUMN_GAME_NAME
                + " = \"" + game_name + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Game game = new Game();

        if (cursor.moveToFirst()){
            game.set_id(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_GAMES, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(game.get_id()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public int getGameCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_GAMES, null);
        cursor.close();

        return cursor.getCount();
    }

    public Cursor getAllGames() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_GAMES + " ORDER BY "
                + COLUMN_GAME_NAME, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
}
