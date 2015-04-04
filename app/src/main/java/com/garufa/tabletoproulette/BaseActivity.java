package com.garufa.tabletoproulette;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Jason on 4/4/2015.
 */
public abstract class BaseActivity extends ActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Display the alert dialog to filter
    protected void displayFilterDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.filter_dialog, null);
        final EditText playersEditText = (EditText) promptView.findViewById(R.id.filter_players_EditText);
        final EditText timeEditText = (EditText) promptView.findViewById(R.id.filter_time_EditText);
        final EditText ratingEditText = (EditText) promptView.findViewById(R.id.filter_rating_EditText);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(promptView)
                .setTitle("Filter")
                .setIcon(R.drawable.ic_action_filter)
                .setCancelable(true).setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), CollectionListView.class);
                intent.putExtra(Constants.EXTRAS_PLAYERS, playersEditText.getText().toString());
                intent.putExtra(Constants.EXTRAS_TIME, timeEditText.getText().toString());
                intent.putExtra(Constants.EXTRAS_RATING, ratingEditText.getText().toString());
                intent.putExtra(Constants.EXTRAS_MECHANIC, "");

                startActivity(intent);
            }
        }).setNegativeButton("Clear Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), CollectionListView.class));
            }
        }).create().show();
    }
    // Display the alert dialog to filter
    protected void displayRandomDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.filter_dialog, null);
        final EditText playersEditText = (EditText) promptView.findViewById(R.id.filter_players_EditText);
        final EditText timeEditText = (EditText) promptView.findViewById(R.id.filter_time_EditText);
        final EditText ratingEditText = (EditText) promptView.findViewById(R.id.filter_rating_EditText);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(promptView)
                .setTitle("Random")
                .setIcon(R.drawable.ic_action_filter)
                .setCancelable(true).setPositiveButton("Roll", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), QueryRandom.class);
                        intent.putExtra(Constants.EXTRAS_PLAYERS, playersEditText.getText().toString());
                        intent.putExtra(Constants.EXTRAS_TIME, timeEditText.getText().toString());
                        intent.putExtra(Constants.EXTRAS_RATING, ratingEditText.getText().toString());
                        intent.putExtra(Constants.EXTRAS_MECHANIC, "");

                        startActivity(intent);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }
    protected void showAlert(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings: return true;
            case R.id.action_collection:
                startActivity(new Intent(this, CollectionListView.class)); break;
            case R.id.action_new_game:
                startActivity(new Intent(this, SearchListView.class)); break;
            case R.id.action_filter:
                displayFilterDialog(); break;
            case R.id.action_random_game:
                displayRandomDialog(); break;
        }

        return super.onOptionsItemSelected(item);
    }
}
