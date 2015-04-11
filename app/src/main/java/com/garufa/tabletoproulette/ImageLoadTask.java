package com.garufa.tabletoproulette;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jason on 3/7/2015.
 */
public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
public AsyncResponse delegate=null;
    private String _url;
    private ImageView _imageView;
    private Bitmap _image;
    private ProgressDialog progressDialog;
    private Context context = null;

    public ImageLoadTask(String url, AsyncResponse delegate) {
        this._url = url;
        this.delegate = delegate;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(_url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        delegate.processFinish(result);
//        _imageView.setImageBitmap(result);
    }

}
