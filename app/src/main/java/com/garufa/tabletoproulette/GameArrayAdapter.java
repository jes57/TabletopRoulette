package com.garufa.tabletoproulette;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Jason on 3/14/2015.
 */
public class GameArrayAdapter extends ArrayAdapter<Game> {

    Context context;
    LayoutInflater inflater;
    List<Game> gameList;
    private SparseBooleanArray mSelectedItemsIds;
    DatabaseHelper dbHandler = DatabaseHelper.getInstance(getContext());

    public GameArrayAdapter(Context context, List<Game> games) {
        super(context, 0, games);
    }
    public GameArrayAdapter(Context context, int resourceId, List<Game> games) {
        super(context, resourceId, games);

        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.gameList = games;
        inflater = LayoutInflater.from(context);
    }

    private class ViewHolder {
        TextView name, year;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_item_simple, null);

            holder.name = (TextView) view.findViewById(R.id.simpleAdapterItem_name);
            holder.year = (TextView) view.findViewById(R.id.simpleAdapterItem_year);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(gameList.get(position).get_name());
        String year = String.valueOf(gameList.get(position).get_year());
        if (year.equals("0")){
            holder.year.setVisibility(View.GONE);
        } else {
            holder.year.setText(year);
        }

        return view;
    }

    public void addFromXml(Game object) {
        String query_url = Constants.URL_BGG_ID_SEARCH + object.get_bgg_id() + Constants.URL_STATS;
        new DownLoadXmlTask().execute(query_url);
    }

    private class DownLoadXmlTask extends AsyncTask<String, Void, Game> {
        @Override
        protected Game doInBackground(String... urls) {
            try {
                return loadXmlFromUrl(urls[0]);
            } catch (IOException e) {
                return new Game("N/A", "Unable to load data: IOException");
            } catch (XmlPullParserException e) {
                return new Game("N/A", "Unable to load data: XmlPullParserException");
            }
        }

        @Override
        protected void onPostExecute(Game g) {
            final Game game_to_add = g;
            dbHandler.addGame(game_to_add);
            // Set page content
            new ImageLoadTask(g.get_image_url(), new AsyncResponse() {
                @Override
                public void processFinish(Bitmap output) {
                    saveImageInternal(output, String.valueOf(game_to_add.get_bgg_id()));
                }
            }).execute();
        }
    }

    private boolean saveImageInternal(Bitmap image, String bgg_id) {
        String file_name = bgg_id + Constants.FILE_TYPE;
        try {
            // Compress the image to write to OutputStream
            FileOutputStream outputStream = context.openFileOutput(file_name, Context.MODE_PRIVATE);

            // Write the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();

            return true;
        } catch (Exception e) {
            Log.e("saveImageInteral()", e.getMessage());
            return false;
        }
    }



    private Game loadXmlFromUrl(String url_string) throws XmlPullParserException, IOException {
        InputStream stream = null;
        BoardGameGeekXmlParser boardGameGeekXmlParser = new BoardGameGeekXmlParser();
        List<Game> games = null;

        try {
            stream = downloadUrl(url_string);
            games = boardGameGeekXmlParser.parse(stream);
        } finally {
            if (stream != null){
                stream.close();
            }
        }
        return games.get(0);
    }

    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
    private InputStream downloadUrl(String url_string) throws IOException {
        URL url = new URL(url_string);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }

    public void remove(Game object) {
        gameList.remove(object);
        notifyDataSetChanged();
    }

    public List<Game> getGameList() {
        return gameList;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value) {
            mSelectedItemsIds.put(position, value);
        } else {
            mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }

    public void selectAll(){
        mSelectedItemsIds = new SparseBooleanArray();
        for (int i = (gameList.size() - 1); i >= 0; i--){
            mSelectedItemsIds.put(i, true);
        }
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getmSelectedItemsIds() { return mSelectedItemsIds; }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // Get the data item for this position
//        Game game = getItem(position);
//        // Check if an existing view is being reused, otherwise inflate the view
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item_simple,
//                    parent, false);
//        }
//
//        // Lookup view for data population
//        TextView textView_name = (TextView) convertView.findViewById(R.id.simpleAdapterItem_name);
//        TextView textView_year = (TextView) convertView.findViewById(R.id.simpleAdapterItem_year);
//
//        // Populate the data into the template view using the data object
//        textView_name.setText(game.get_name());
//        String year = String.valueOf(game.get_year());
//        if (year.equals("0")) {
//            textView_year.setVisibility(View.GONE);
//        } else {
//            textView_year.setText(year);
//        }
//        // Return the completed view to render on screen
//        return convertView;
//    }
}
